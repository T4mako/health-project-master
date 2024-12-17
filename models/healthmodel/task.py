import os
import json
from celery import shared_task
from datetime import datetime, timedelta
import requests
import pymysql

# 查询数据任务
@shared_task
def fetch_health_data():
    url = "http://www.pahealthsys.cn/device/deviceData/getDataByType"
    headers = {
        "Authorization": "Bearer 83e60402-b255-4f7d-87ac-139c6564f250",
        "Content-Type": "application/json"
    }
    types = ["temperature", "heartRate", "breathRate", "bloodOxygen", "bloodGlucose", "bloodPressure"]
    start_date = datetime.now() - timedelta(days=1)
    end_date = datetime.now() - timedelta(days=1)

    all_records = []

    for data_type in types:
        current_date = start_date
        while current_date <= end_date:
            date_str = current_date.strftime("%Y-%m-%d")
            payload = {
                "current": 1,
                "size": 100,
                "startDate": date_str,
                "endDate": date_str,
                "type": data_type
            }

            try:
                response = requests.post(url, headers=headers, json=payload)
                response.raise_for_status()
                response_data = response.json()

                if response_data.get("code") == 0 and response_data.get("data"):
                    records = response_data["data"].get("records", [])
                    all_records.extend(records)
                else:
                    print(f"接口返回错误，类型: {data_type}, 日期: {date_str}, 响应: {response_data}")
            except requests.RequestException as e:
                print(f"请求失败，类型: {data_type}, 日期: {date_str}, 错误: {e}")

            current_date += timedelta(days=1)

    output_file = "all_records.json"
    with open(output_file, "w", encoding="utf-8") as file:
        json.dump(all_records, file, ensure_ascii=False, indent=4)

    return f"数据已保存到 {output_file}"


# 插入数据任务
@shared_task
def upsert_health_data():
    db_config = {
        "host": "localhost",
        "user": "root",
        "password": "root",
        "database": "health_test",
    }
    json_file = "all_records.json"

    connection = pymysql.connect(
        host=db_config["host"],
        user=db_config["user"],
        password=db_config["password"],
        database=db_config["database"],
    )
    cursor = connection.cursor()

    try:
        with open(json_file, 'r', encoding='utf-8') as file:
            data = json.load(file)

        for record in data:
            create_date = record["create_time"].split(" ")[0]
            family_user_id = record["family_user_id"]
            researched_person_id = record["researched_person_id"]

            fields_to_check = {
                "temperature": record["temperature"],
                "heart_rate": record["heart_rate"],
                "breath_rate": record["breath_rate"],
                "blood_oxygen": record["blood_oxygen"],
                "blood_glucose": record["blood_glucose"],
                "systolic": record["systolic"],
                "diastolic": record["diastolic"],
            }
            fields_to_update = {k: v for k, v in fields_to_check.items() if v is not None}

            check_query = """
                SELECT id FROM health_data
                WHERE create_time = %s AND family_user_id = %s AND researched_person_id = %s
            """
            cursor.execute(check_query, (create_date, family_user_id, researched_person_id))
            existing_record = cursor.fetchone()

            if existing_record:
                record_id = existing_record[0]
                if fields_to_update:
                    update_query = f"""
                        UPDATE health_data
                        SET {', '.join(f"{key} = %s" for key in fields_to_update.keys())}
                        WHERE id = %s
                    """
                    cursor.execute(update_query, (*fields_to_update.values(), record_id))
            else:
                fields_to_insert = {
                    "create_time": create_date,
                    "family_user_id": family_user_id,
                    "researched_person_id": researched_person_id,
                    **fields_to_update,
                }
                insert_query = f"""
                    INSERT INTO health_data ({', '.join(fields_to_insert.keys())})
                    VALUES ({', '.join(['%s'] * len(fields_to_insert))})
                """
                cursor.execute(insert_query, tuple(fields_to_insert.values()))

        connection.commit()
        return "数据插入或更新成功！"

    except Exception as e:
        connection.rollback()
        return f"操作失败：{e}"

    finally:
        cursor.close()
        connection.close()
