# predictions/serializers.py

from rest_framework import serializers

class PredictionRequestSerializer(serializers.Serializer):
    model_type = serializers.ChoiceField(choices=["stroke", "heart"])
    input_array = serializers.ListField()
