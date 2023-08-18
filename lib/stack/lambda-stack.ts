import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import * as s3 from "aws-cdk-lib/aws-s3";
import { getAccountUniqueName } from "../config/accounts";
import { ChallengeCdkStackProps } from "../hello-cdk-stack";
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

export class ChallengeApiStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props: ChallengeCdkStackProps) {
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

    //사용자 확인
    const confirmUserFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-confirm-user`,
      {
        functionName: `${getAccountUniqueName(props.context)}-confirm-user`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        index: "signup_confirm.py",
        handler: "lambda_handler",
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        environment: {
          TABLE_NAME: props.dynamoStack!.User.tableName,
        },
      }
    );

    //사용자 로그인
    const loginUserFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-login-user`,
      {
        functionName: `${getAccountUniqueName(props.context)}-login-user`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/user"),
        index: "login_user.py",
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
    const createChallengeFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-create-challenge`,
      {
        functionName: `${getAccountUniqueName(props.context)}-create-challenge`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/challenge"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "create_challenge.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.challenge.tableName,
        },
      }
    );

    //TODO 조회
    const readChallengeFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-read-challenge`,
      {
        functionName: `${getAccountUniqueName(props.context)}-read-challenge`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/challenge"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "read_challenge.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.challenge.tableName,
        },
      }
    );

    //TODO 업데이트
    const updateChallengeFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-update-challenge`,
      {
        functionName: `${getAccountUniqueName(props.context)}-update-challenge`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/challenge"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "update_challenge.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.challenge.tableName,
        },
      }
    );

    //TODO 삭제 ->
    const deleteChallengeFunction = new PythonFunction(
      this,
      `${SYSTEM_NAME}-delete-challenge`,
      {
        functionName: `${getAccountUniqueName(props.context)}-delete-challenge`,
        entry: path.join("C://Users//annie/hello-cdk/app/backend/challenge"),
        runtime: Runtime.PYTHON_3_10,
        role: lambdaRole,
        index: "delete_challenge.py", // file name
        handler: "lambda_handler", // function name
        environment: {
          TABLE_NAME: props.dynamoStack!.challenge.tableName,
        },
      }
    );

    const api = new RestApi(this, `${SYSTEM_NAME}-api`, {
      restApiName: `${getAccountUniqueName(props.context)}-challenge-api`,
      description: "challenge Application API",
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
              )}-challenge-api`,
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
      description: "challenge API Key",
    });

    const usagePlan = api.addUsagePlan(`${SYSTEM_NAME}-UsagePlan`, {
      name: `${getAccountUniqueName(props.context)}-UsagePlan`,
      description: "challenge Usage Plan",
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

    const userResource = api.root.addResource("signup");
    userResource.addMethod(
      "POST",
      new LambdaIntegration(createUserFunction),
      methodOptions
    );

    const confirmResource = api.root.addResource("confirm");
    confirmResource.addMethod(
      "POST",
      new LambdaIntegration(confirmUserFunction),
      methodOptions
    );

    const loginResource = api.root.addResource("login");
    loginResource.addMethod(
      "POST",
      new LambdaIntegration(loginUserFunction),
      methodOptions
    );

    const readResource = api.root.addResource("readUser");
    readResource.addMethod(
      "GET",
      new LambdaIntegration(readUserFunction),
      methodOptions
    );

    const userInfoResource = api.root.addResource("updateUserInfo");
    userInfoResource.addMethod(
      "PUT",
      new LambdaIntegration(updateUserInfoFunction),
      methodOptions
    );

    const createChallengeResource = api.root.addResource("createChallenge");
    createChallengeResource.addMethod(
      "POST",
      new LambdaIntegration(createChallengeFunction),
      methodOptions
    );

    const readChallengeResource = api.root.addResource("readChallenge");
    readChallengeResource.addMethod(
      "POST",
      new LambdaIntegration(readChallengeFunction),
      methodOptions
    );

    const updateChallengeResource = api.root.addResource("updateChallenge");
    updateChallengeResource.addMethod(
      "PUT",
      new LambdaIntegration(updateChallengeFunction),
      methodOptions
    );

    const deleteChallengeResource = api.root.addResource("deleteChallenge");
    deleteChallengeResource.addMethod(
      "DELETE",
      new LambdaIntegration(deleteChallengeFunction),
      methodOptions
    );
  }
}
