import json
import boto3
import os
from boto3.dynamodb.conditions import Key


def getTable():
    
    table_name = os.getenv("TABLE_NAME")
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    
    return table

def lambda_handler(event, context):
    
    table = getTable()
    
    userSerialNum = event['userSerialNum']
    type = event['updateAttrType']
    content = event['content']

    #userId 수정 불가
    if type == "userId" :
        return {
            'statusCode': 200,
            'body': json.dumps(f'{type} fail!')
        }
    #friend Id 추가
    elif type == "friends" :
        update_userFriend(userSerialNum,content)
    #goalList 추가
    elif type == "goalList" :
        response = table.query(
            KeyConditionExpression=Key('userSerialNum').eq(userSerialNum)
        )
        convert_regular_json = json.loads(json.dumps(response))
        count= len(convert_regular_json["Items"][0][type])
        update_usergoingTodo(table,userSerialNum,content,count)
    else :
        table.update_item(
            Key={
                'userSerialNum':userSerialNum
            },
            UpdateExpression='set #updateType= :content',
            ExpressionAttributeNames={
                '#updateType':type
            },
            ExpressionAttributeValues={
                ':content':content
            }
        )
    return {
        'statusCode': 200,
        'body': json.dumps(f'{userSerialNum}{type}{content}update!')
    }


#친구 추가
def update_userFriend(table,userSerialNum,content):
    
    table.update_item(
        Key={
            'userSerialNum': userSerialNum
        },
        UpdateExpression="set #friends=list_append(#friends,:data)",
        ExpressionAttributeNames={
            "#friends":"friends"
        },
        ExpressionAttributeValues={
            ":data":[content]
        }
    )

    table.update_item(
        Key={
            'userSerialNum': content
        },
        UpdateExpression="set #friends=list_append(#friends,:data)",
        ExpressionAttributeNames={
            "#friends":"friends"
        },
        ExpressionAttributeValues={
            ":data":[userSerialNum]
        }
    )

#종료된 todo 사용자 info에 저장
def update_usergoingTodo(table,userSerialNum,content,count):
    
    table.update_item(
        Key={
            'userSerialNum': userSerialNum
        },
        UpdateExpression="set #goalList.#count=:data",
        ExpressionAttributeNames={
            "#goalList":"goalList",
            "#count":str(count+1)
        },
        ExpressionAttributeValues={
            ":data":content
        }
    )