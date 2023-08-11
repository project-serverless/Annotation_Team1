import json
import boto3
import os

def lambda_handler(event,context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    serialNum = event['serialNum']
    friendId=event['friendId']
    
    table.update_item(
        Key={
            'serialNum':serialNum
        },
        UpdateExpression='set #friends= :friend',
        ExpressionAttributeNames={
            '#friends':'friends'
        },
        ExpressionAttributeValues={
            ':friend':[friendId]
        }
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps(f'{serialNum} update!')
    }