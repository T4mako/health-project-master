from __future__ import absolute_import, unicode_literals
import os
from celery import Celery

# 设置 Django 配置环境变量
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'healthmodel.settings')

# 创建 Celery 实例
app = Celery('healthdata')

# 配置 Celery 使用单机模式
app.config_from_object('django.conf:settings', namespace='CELERY')

# 自动发现任务
app.autodiscover_tasks()


from celery.schedules import crontab

app.conf.beat_schedule = {
    'fetch_health_data_daily': {
        'task': 'healthmodel.tasks.fetch_health_data',
        'schedule': crontab(minute=0, hour=0),  # 每天0点执行
    },
    'upsert_health_data_daily': {
        'task': 'healthmodel.tasks.upsert_health_data',
        'schedule': crontab(minute=0, hour=1),  # 每天1点执行
    },
}

