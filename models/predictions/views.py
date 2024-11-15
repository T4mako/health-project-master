from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import joblib
import numpy as np
import os

# 获取当前文件的绝对路径
current_dir = os.path.dirname(os.path.abspath(__file__))

# 构建模型文件的绝对路径
stroke_model_path = os.path.join(current_dir, '..', 'models', 'stroke', 'stroke_model.pkl')
stroke_model = joblib.load(stroke_model_path)

scaler_path = os.path.join(current_dir, '..', 'models', 'stroke', 'scaler_stroke.pkl')
scaler = joblib.load(scaler_path)

label_encoder_path = os.path.join(current_dir, '..', 'models', 'stroke', 'label_encoder.pkl')
label_encoder = joblib.load(label_encoder_path)

diabetes_features_path = os.path.join(current_dir, '..', 'models', 'diabetes', 'diabetes_features.pkl')
diabetes_features = joblib.load(diabetes_features_path)

diabetes_logreg_model_path = os.path.join(current_dir, '..', 'models', 'diabetes', 'diabetes_logreg_model.pkl')
diabetes_logreg_model = joblib.load(diabetes_logreg_model_path)

heart_disease_features_path = os.path.join(current_dir, '..', 'models', 'heart_disease', 'heart_disease_features.pkl')
heart_disease_features = joblib.load(heart_disease_features_path)

heart_disease_rf_model_path = os.path.join(current_dir, '..', 'models', 'heart_disease', 'heart_disease_rf_model.pkl')
heart_disease_rf_model = joblib.load(heart_disease_rf_model_path)


# Preprocessing function
def preprocess_input_and_standardize(systolic, diastolic, blood_glucose_mmol_L, gender, bmi, age):
    # Preprocess the input data (encode gender and check hypertension status)
    hypertension = 1 if systolic >= 140 or diastolic >= 90 else 0
    blood_glucose_mg_dL = blood_glucose_mmol_L * 18
    gender_encoded = label_encoder.transform([gender])[0]

    # Prepare the input array
    input_data = np.array([[gender_encoded, age, hypertension, blood_glucose_mg_dL, bmi]])

    # Standardize the input data using a pre-trained scaler
    return scaler.transform(input_data)


from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json


@csrf_exempt  # Disable CSRF for this view (only for testing, not recommended in production)
def predict_stroke(request):
    if request.method == 'POST':
        try:
            # Parse the JSON body to extract input data
            input_data = json.loads(request.body)

            if not isinstance(input_data, list):
                return JsonResponse({'error': 'Request body must be an array of input data'}, status=400)

            response_data = []

            for data in input_data:
                id = data.get('id')
                # Extract the individual values for each entry in the array
                systolic = float(data.get('systolic'))
                diastolic = float(data.get('diastolic'))
                blood_glucose_mmol_L = float(data.get('blood_glucose'))
                blood_glucose_mmol_L = blood_glucose_mmol_L * 18  # Convert to mg/dL
                gender = data.get('gender')
                gender = 'Male' if gender == '男' else 'Female'
                bmi = float(data.get('bmi'))
                age = int(data.get('age'))

                # Ensure all required parameters are provided
                if not all([systolic, diastolic, blood_glucose_mmol_L, gender, bmi, age]):
                    return JsonResponse({'error': 'Missing required parameters in one of the input entries'}, status=400)

                # Preprocess the input data and standardize it
                processed_input_scaled = preprocess_input_and_standardize(systolic, diastolic, blood_glucose_mmol_L, gender, bmi, age)

                # Make prediction
                prediction = stroke_model.predict(processed_input_scaled)[0]
                probability = stroke_model.predict_proba(processed_input_scaled)[0][1]  # Probability of stroke (class 1)

                # Append the result to the response data
                response_data.append({
                    'id': id,
                    'stroke_prediction': 'Yes' if prediction == 1 else 'No',
                    'probability': probability
                })

            return JsonResponse(response_data, safe=False)

        except Exception as e:
            return JsonResponse({'error': str(e)}, status=400)

    return JsonResponse({'error': 'Only POST method is allowed'}, status=405)


@csrf_exempt
def predict_diabetes(request):
    if request.method == 'POST':
        try:
            # Parse the JSON body to extract input data
            input_data = json.loads(request.body)

            if not isinstance(input_data, list):
                return JsonResponse({'error': 'Request body must be an array of input data'}, status=400)

            response_data = []

            for data in input_data:
                id = data.get('id')
                glucose = float(data.get('blood_glucose'))
                glucose = glucose * 18  # Convert to mg/dL
                blood_pressure = float(data.get('diastolic'))
                bmi = float(data.get('bmi'))
                age = int(data.get('age'))

                # Ensure all required parameters are provided
                if not all([glucose, blood_pressure, bmi, age]):
                    return JsonResponse({'error': 'Missing required parameters in one of the input entries'},
                                        status=400)

                # Prepare input data
                input_array = np.array([[glucose, blood_pressure, bmi, age]])

                # Make prediction
                prediction = diabetes_logreg_model.predict(input_array)[0]
                probability = diabetes_logreg_model.predict_proba(input_array)[0][
                    1]  # Probability of diabetes (class 1)

                # Append the result to the response data
                response_data.append({
                    'id': id,
                    'diabetes_prediction': 'Yes' if prediction == 1 else 'No',
                    'probability': probability
                })

            return JsonResponse(response_data, safe=False)

        except Exception as e:
            return JsonResponse({'error': str(e)}, status=400)

    return JsonResponse({'error': 'Only POST method is allowed'}, status=405)

@csrf_exempt
def predict_heart_disease(request):
    if request.method == 'POST':
        try:
            # Parse the JSON body to extract input data
            input_data = json.loads(request.body)

            if not isinstance(input_data, list):
                return JsonResponse({'error': 'Request body must be an array of input data'}, status=400)

            response_data = []

            for data in input_data:
                id = data.get('id')
                age = int(data.get('age'))
                sex = str(data.get('gender'))  # 1 for male, 0 for female
                sex = 1 if sex == '男' else 0
                trestbps = float(data.get('systolic'))
                fbs = float(data.get('blood_glucose'))
                # 1 if fasting blood sugar > 120 mg/dL, else 0
                fbs = 1 if fbs * 18 > 120 else 0

                # Prepare input data
                input_array = np.array([[age, sex, trestbps, fbs]])

                # Make prediction
                prediction = heart_disease_rf_model.predict(input_array)[0]
                probability = heart_disease_rf_model.predict_proba(input_array)[0][
                    1]  # Probability of heart disease (class 1)

                # Append the result to the response data
                response_data.append({
                    'id': id,
                    'heart_disease_prediction': 'Yes' if prediction == 1 else 'No',
                    'probability': probability
                })

            return JsonResponse(response_data, safe=False)

        except Exception as e:
            return JsonResponse({'error': str(e)}, status=400)

    return JsonResponse({'error': 'Only POST method is allowed'}, status=405)
