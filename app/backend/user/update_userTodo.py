import json
import boto3
import os


def lambda_handler(event,context):
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    serialNum = event['serialNum']
    date1=event['date1']
    result1=event['result1']
    date2=event['date2']
    result2=event['result2']
    date3=event['date3']
    result3=event['result3']
    goalContent=event['goalContent']
    month=event['month']
    goalNum = 'g'+str(0)
    
    table.update_item(
        
        Key={
            'serialNum':event['serialNum']
        },
        UpdateExpression='set #goalList=:goalSetting',
        ExpressionAttributeNames={
            '#goalList' : 'goalList'
        },
        ExpressionAttributeValues={
            ':goalSetting':  
                {
                    goalNum: {
                        'date':{
                            'First':
                                {
                                    'date':date1,
                                    'status':result1
                                }
                            ,
                            'Second':
                                {
                                    'date':date2,
                                    'status':result2
                                }
                            ,
                            'Third':
                                {
                                    'date':date3,
                                    'status':result3
                                }
                                
                            
                        },
                        'goalContent':goalContent,
                        'month':month
                    },
                }
        }
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps(f'{serialNum} update!')
    }