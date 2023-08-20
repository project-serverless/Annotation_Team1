import json
import boto3
import os

def lambda_handler(event, context):
    
    client = boto3.client('cognito-idp')
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    access_token = event['access_token']

    result = client.get_user(
            AccessToken = access_token
    )

    userId = result['Username']
      
    table.delete_item(
        
        Key={
            'userId':userId
        } 
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps(f'{userId} delete!')
    }