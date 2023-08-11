import json
import boto3
import os

def lambda_handler(event, context):
    

    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    serialNum = event['serialNum']
    
    table.delete_item(
        
        Key={
            'serialNum':serialNum
        }
        
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps(f'{serialNum} delete!')
    }