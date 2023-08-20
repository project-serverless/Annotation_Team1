import json
import boto3
import os
from boto3.dynamodb.conditions import Key

def lambda_handler(event, context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    todoSerialNum = event['todoSerialNum']
    
    table.delete_item(
        
        Key={
            'todoSerialNum':todoSerialNum,
        }
        
    )

    

    return {
        'statusCode': 200,
        'body': json.dumps(f'{todoSerialNum} delete!')
    }