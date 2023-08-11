import json
import boto3
import os
from boto3.dynamodb.conditions import Key

def lambda_handler(event, context):
    

    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    userSerialNum = event['userSerialNum']
    finalDate = event['finalDate']
    
#  item = table.query(
#     KeyConditionExpression= 'userSerialNum'= :'userSerialNum' and 'finalDate'.eq(event['nowdate']),
#        ExpressionAttributeNames={
#            '#status':event['status']
#        },
#        ExpressionAttributeValues={
#            ':statusCode':True,
#        }
#    )
    
    
    table.delete_item(
        
        Key={
            'userSerialNum':userSerialNum,
            'finalDate':finalDate
        }
        
    )

    return {
        'statusCode': 200,
        'body': json.dumps(f'{userSerialNum} delete!')
    }