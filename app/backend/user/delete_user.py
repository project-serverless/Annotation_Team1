import json
import boto3
import os

def lambda_handler(event, context):
    

    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    userSerialNum = event['userSerialNum']
    
    table.delete_item(
        
        Key={
            'userSerialNum':userSerialNum
        } 
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps(f'{userSerialNum} delete!')
    }