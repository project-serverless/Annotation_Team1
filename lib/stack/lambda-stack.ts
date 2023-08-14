import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import * as s3 from "aws-cdk-lib/aws-s3";
import { getAccountUniqueName } from "../config/accounts";
import { Todo3DaysCdkStackProps } from "../hello-cdk-stack";
import { SYSTEM_NAME } from "../config/commons";
import { PythonFunction } from "@aws-cdk/aws-lambda-python-alpha";
import { Runtime } from "aws-cdk-lib/aws-lambda";
import * as path from "path";
import {
  ManagedPolicy,
  Role,
  ServicePrincipal,
  CompositePrincipal,
  PolicyDocument,
  PolicyStatement,
  Effect,
} from "aws-cdk-lib/aws-iam";
import {
  RestApi,
  EndpointType,
  MethodLoggingLevel,
  LogGroupLogDestination,
  LambdaIntegration,
} from "aws-cdk-lib/aws-apigateway";
import { LogGroup } from "aws-cdk-lib/aws-logs";

export class Todo3DaysApiStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props: Todo3DaysCdkStackProps) {
    super(scope, id, props);

    const lambdaRole = new Role(this, `${SYSTEM_NAME}-lambda-role`, {
      roleName: `${getAccountUniqueName(props.context)}-lambda-role`,
      assumedBy: new CompositePrincipal(
        new ServicePrincipal("lambda.amazonaws.com")
      ),
      managedPolicies: [
        ManagedPolicy.fromAwsManagedPolicyName(
          "service-role/AWSLambdaBasicExecutionRole"
        ),
        ManagedPolicy.fromAwsManagedPolicyName("AmazonS3FullAccess"),
        ManagedPolicy.fromAwsManagedPolicyName("AmazonDynamoDBFullAccess"),
      ],
    });

    //사용자 생성
    const createUserFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-create-user`,
      {
        functionName: `${getAccountUniqueName(props.context)}-create-user`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        index: "create_user.py",
        handler: "lambda_handler",
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        environment: {
          TABLE_NAME: props.dynamoStack!.User.tableName,
        },
      }
    );

    //사용자 조회
    const readUserFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-read-user`,
      {
        functionName: `${getAccountUniqueName(props.context)}-read-user`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        index: "read_user.py",
        handler: "lambda_handler",
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        environment: {
          TABLE_NAME: props.dynamoStack!.User.tableName,
        },
      }
    );

    //사용자 정보 업데이트 - friend 추가
    const updateUserFriendFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-update-userfriends`,
      {
        functionName: `${getAccountUniqueName(
          props.context
        )}-update-userfriends`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        index: "update_userFriends.py",
        handler: "lambda_handler",
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        environment: {
          TABLE_NAME: props.dynamoStack!.User.tableName,
        },
      }
    );

    //사용자 정보 업데이트 - Todo 추가
    const updateUserTodoFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-update-usertodo`,
      {
        functionName: `${getAccountUniqueName(props.context)}-update-usertodo`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "update_userTodo.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.User.tableName,
        },
      }
    );

    //사용자 정보 업데이트 - 사용자 관련 정보 수정 가능
    const updateUserInfoFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-update-userinfo`,
      {
        functionName: `${getAccountUniqueName(props.context)}-update-userinfo`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "update_userInfo.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.User.tableName,
        },
      }
    );

    //TODO 생성
    const createGoingTodoFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-create-goingTodo`,
      {
        functionName: `${getAccountUniqueName(props.context)}-create-goingTodo`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/goingTodo"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "create_goingTodo.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.goingTodo.tableName,
        },
      }
    );

    //TODO 조회
    const readGoingTodoFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-read-goingTodo`,
      {
        functionName: `${getAccountUniqueName(props.context)}-read-goingTodo`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/goingTodo"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "read_goingTodo.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.goingTodo.tableName,
        },
      }
    );

    //TODO 업데이트
    const updateGoingTodoTodoFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-update-goingTodo`,
      {
        functionName: `${getAccountUniqueName(props.context)}-update-goingTodo`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/goingTodo"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "update_goingTodo.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.goingTodo.tableName,
        },
      }
    );

    //TODO 삭제 ->
    const deleteGoingTodoTodoFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-delete-goingTodo`,
      {
        functionName: `${getAccountUniqueName(props.context)}-delete-goingTodo`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/goingTodo"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "delete_goingTodo.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.goingTodo.tableName,
        },
      }
    );

    const api = new RestApi(this, `${SYSTEM_NAME}-api`, {
      restApiName: `${getAccountUniqueName(props.context)}-todo3days-api`,
      description: "Todo3Days Application API",
      deployOptions: {
        stageName: "dev",
        metricsEnabled: true,
        loggingLevel: MethodLoggingLevel.INFO,
        accessLogDestination: new LogGroupLogDestination(
          new LogGroup(
            this,
            `${getAccountUniqueName(props.context)}-api-log-group`,
            {
              logGroupName: `/API-Gateway/${getAccountUniqueName(
                props.context
              )}-todo3days-api`,
              removalPolicy: props.terminationProtection
                ? cdk.RemovalPolicy.RETAIN
                : cdk.RemovalPolicy.DESTROY,
            }
          )
        ),
      },
      endpointTypes: [EndpointType.REGIONAL],
      retainDeployments: props.terminationProtection,
      cloudWatchRole: true,
    });

    const apiKey = api.addApiKey(`${SYSTEM_NAME}-ApiKey`, {
      apiKeyName: `${getAccountUniqueName(props.context)}-ApiKey`,
      description: "Todo3Days API Key",
    });

    const usagePlan = api.addUsagePlan(`${SYSTEM_NAME}-UsagePlan`, {
      name: `${getAccountUniqueName(props.context)}-UsagePlan`,
      description: "Todo3Days Usage Plan",
      apiStages: [
        {
          api: api,
          stage: api.deploymentStage,
        },
      ],
    });
    usagePlan.addApiKey(apiKey);

    const methodOptions = {
      apiKeyRequired: true,
    };
    updateUserInfoFunction;

    const userResource = api.root.addResource("createUser");
    userResource.addMethod(
      "POST",
      new LambdaIntegration(createUserFunction),
      methodOptions
    );

    const loginResource = api.root.addResource("readUser");
    loginResource.addMethod(
      "POST",
      new LambdaIntegration(readUserFunction),
      methodOptions
    );

    const updateUserFriendResource = api.root.addResource("userFriend");
    updateUserFriendResource.addMethod(
      "PUT",
      new LambdaIntegration(updateUserFriendFunction),
      methodOptions
    );

    const updateUserTodoResource = api.root.addResource("userTodo");
    updateUserTodoResource.addMethod(
      "PUT",
      new LambdaIntegration(updateUserTodoFunction),
      methodOptions
    );

    const userInfoResource = api.root.addResource("updateUserInfo");
    userInfoResource.addMethod(
      "PUT",
      new LambdaIntegration(updateUserInfoFunction),
      methodOptions
    );

    const createGoingTodoResource = api.root.addResource("createTodo");
    createGoingTodoResource.addMethod(
      "POST",
      new LambdaIntegration(createGoingTodoFunction),
      methodOptions
    );

    const readGoingTodoResource = api.root.addResource("readTodo");
    readGoingTodoResource.addMethod(
      "POST",
      new LambdaIntegration(readGoingTodoFunction),
      methodOptions
    );

    const updateGoingTodoResource = api.root.addResource("updateTodo");
    updateGoingTodoResource.addMethod(
      "PUT",
      new LambdaIntegration(updateGoingTodoTodoFunction),
      methodOptions
    );

    const deleteGoingTodoResource = api.root.addResource("deleteTodo");
    deleteGoingTodoResource.addMethod(
      "DELETE",
      new LambdaIntegration(deleteGoingTodoTodoFunction),
      methodOptions
    );
  }
}
