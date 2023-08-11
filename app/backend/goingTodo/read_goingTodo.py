import json
import boto3
import os
from boto3.dynamodb.conditions import Key

def lambda_handler(event, context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    serialNum = event['serialNum']
    
    response = table.query(
        KeyConditionExpression=Key('serialNum').eq(serialNum)
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps(response)
    }

