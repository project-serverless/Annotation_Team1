import json
from datetime import datetime
from datetime import timedelta
import dateutil.tz
import boto3
from boto3.dynamodb.conditions import Key

def check_challenge(user):
    #3일 모두 True
    if user["GoalArr"][0] == True and user["GoalArr"][1] == True and user["GoalArr"][2] == True :
        SuccessGoal = int(user["SuccessGoal"])+1
        TotalGoal = int(user["TotalGoal"])+1
        ContinueGoal = int(user["TotalGoal"])+1
    else: #하루라도 true가 아닐 경우
        SuccessGoal = int(user["SuccessGoal"])
        TotalGoal = int(user["TotalGoal"])+1
        ContinueGoal = 0

    return SuccessGoal,TotalGoal,ContinueGoal

#challenge, user table에 현재 진행 결과 저장
def set_table(user,usertable,date,status):
    table_name = "team1-ICN-challenge-table"
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)


    response = table.query(
        KeyConditionExpression=Key('todoSerialNum').eq(user['Goal'])
    )
    challenge = response["Items"][0]

    finalDate = challenge['finalDate']
    day = finalDate.split('-')[2]
    SuccessGoal=user["SuccessGoal"]
    TotalGoal=user["TotalGoal"]
    ContinueGoal=user["ContinueGoal"]

    
    if int(day)-int(date.day) == 2:
        Level = "day1"
    elif int(day)-int(date.day) == 1:
        Level = "day2"
    elif int(day)-int(date.day) == 0:
        Level = "day3"
    else:
        Level = "Fin"
    
    if Level != "Fin":
        table.update_item(
            Key={
                'todoSerialNum': user['Goal']
            },
            UpdateExpression="set #GoalArr.#level=:data",
            ExpressionAttributeNames={
                "#GoalArr":"GoalArr",
                "#level":Level
            },
            ExpressionAttributeValues={
                ":data":{
                    "date":date.strftime('%Y-%m-%d'),
                    "status":status
                }
            }
        )

        index = int(Level[3])-1
        list = user['GoalArr']
        list[index]=True

        usertable.update_item(
            Key={
                'userId': user['userId']
            },
            UpdateExpression="set #GoalArr=:data",
            ExpressionAttributeNames={
                "#GoalArr":"GoalArr"
            },
            ExpressionAttributeValues={
                ":data":list
            }
        )

        #마지막날일경우 전체 진행 확인 후 goal 숫자 수정 
        if index == 2:
            SuccessGoal,TotalGoal,ContinueGoal = check_challenge(user)
        else : #마지막날 아니니 그대로 유지
            SuccessGoal =user["SuccessGoal"]
            TotalGoal=user["TotalGoal"]
            ContinueGoal=user["TotalGoal"]
    else: #data 리셋을 위함
        usertable.update_item(
            Key={
                'userId': user['userId']
            },
            UpdateExpression="set #Goal=:data",
            ExpressionAttributeNames={
                "#Goal":"Goal"
            },
            ExpressionAttributeValues={
                ":data":"NONE"
            }
        )
        
    return SuccessGoal, TotalGoal,ContinueGoal
    
def data_setting(table,user,SuccessGoal,TotalGoal,ContinueGoal):
    table.update_item(
            Key={
            'userId': user['userId']
            },
            UpdateExpression="set #SuccessGoal=:SuccessGoal,#TotalGoal=:TotalGoal,#ContinueGoal=:ContinueGoal",
            ExpressionAttributeNames={
                "#SuccessGoal":"SuccessGoal",
                "#TotalGoal":"TotalGoal",
                "#ContinueGoal":"ContinueGoal"
            },
            ExpressionAttributeValues={
                ":TotalGoal":int(TotalGoal),
                ":SuccessGoal":int(SuccessGoal),
                ":ContinueGoal":int(ContinueGoal)
            }
        )
        
def lambda_handler(event, context):
    client = boto3.client('cognito-idp')
    table_name = "team1-ICN-user-table"
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)
    #header = event['headers']['Authorization']
    asia = dateutil.tz.gettz('Asia/Seoul')
    date = datetime.now(tz=asia)
    
    #result = client.get_user(
    #        AccessToken = header
    #)
    
    response = table.query(
        KeyConditionExpression=Key('userId').eq('Dongmin')
    )
    user = response["Items"][0] #user 테이블
    
    if date.hour == 0 and date.minute == 0: #0시0분일 경우 -> TodaySuccess False 변경
        table.update_item(
            Key={
                'userId': user['userId']
            },
            UpdateExpression="set #TodaySuccess=:statusCode",
            ExpressionAttributeNames={
                "#TodaySuccess":"TodaySuccess"
            },
            ExpressionAttributeValues={
                ":statusCode":False
            }
        )
    else : #아닐 경우 미션 성공에 의한 변경임
        status = False
        table.update_item(
            Key={
                'userId': user['userId']
            },
            UpdateExpression="set #TodaySuccess=:statusCode",
            ExpressionAttributeNames={
                "#TodaySuccess":"TodaySuccess"
            },
            ExpressionAttributeValues={
                ":statusCode":True
            }
        )
        status = True
    
    #successgoal, totalgoal, continuegoal 설정
    SuccessGoal,TotalGoal,ContinueGoal = set_table(user,table,date,status)
    
    data_setting(table,user,SuccessGoal,TotalGoal,ContinueGoal)
      
    return{
        "body":"Hello Lambda"
    }

