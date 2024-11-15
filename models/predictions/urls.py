from django.urls import path
from . import views

urlpatterns = [
    path('predict_stroke', views.predict_stroke, name='predict_stroke'),
    path('predict_diabetes', views.predict_diabetes, name='predict_diabetes'),
    path('predict_heart', views.predict_heart_disease, name='predict_heart'),
]
