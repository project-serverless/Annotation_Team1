import json
import boto3
import os
from boto3.dynamodb.conditions import Key

def lambda_handler(event, context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    userSerialNum = event['userSerialNum']

    response = table.query(
        KeyConditionExpression=Key('userSerialNum').eq(userSerialNum)
    )
    
    convert_regular_json = json.loads(json.dumps(response))

    return {
        'statusCode': 200,
        'body': convert_regular_json["Items"]
    }

#항목 개수 : len(convert_regular_json["Items"][0][type])
#항목 상세정보 : len(convert_regular_json["Items"][0][type]
