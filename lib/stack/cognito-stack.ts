import * as cdk from "aws-cdk-lib";
import * as cognito from "aws-cdk-lib/aws-cognito";
import { Construct } from "constructs";
import { Todo3DaysCdkStackProps } from "../hello-cdk-stack";
import { SYSTEM_NAME } from "../config/commons";

export class Todo3DaysCdkCognitoStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: Todo3DaysCdkStackProps) {
    super(scope, id, props);

    const userpool = new cognito.UserPool(this, `${SYSTEM_NAME}-userpool`, {
      userPoolName: `${SYSTEM_NAME}-userpool`,
      signInAliases: {
        email: true,
      },
      selfSignUpEnabled: true,
      autoVerify: {
        email: true,
      },
      userVerification: {
        emailSubject: "You need to verify your email",
        emailBody: "Thanks for signing up Your verification code is {####}", // # This placeholder is a must if code is selected as preferred verification method
        emailStyle: cognito.VerificationEmailStyle.CODE,
      },
      standardAttributes: {
        email: {
          mutable: true,
          required: true,
        },
      },
      passwordPolicy: {
        minLength: 8,
        requireLowercase: true,
        requireUppercase: false,
        requireDigits: true,
        requireSymbols: false,
      },
      accountRecovery: cognito.AccountRecovery.EMAIL_ONLY,
      deletionProtection: true,
      removalPolicy: cdk.RemovalPolicy.DESTROY,
    });

    const userPoolDomain = new cognito.UserPoolDomain(
      this,
      `${SYSTEM_NAME}-userpoolDomain`,
      {
        userPool: userpool,
        cognitoDomain: {
          domainPrefix: "todo3days",
        },
      }
    );

    const appClient = new cognito.UserPoolClient(
      this,
      `${SYSTEM_NAME}-userpoolClient`,
      {
        userPool: userpool,
        userPoolClientName: `${SYSTEM_NAME}-userpoolClient`,
        authFlows: {
          userPassword: true,
        },
        oAuth: {
          flows: {
            authorizationCodeGrant: false,
            implicitCodeGrant: true,
          },
          scopes: [
            cognito.OAuthScope.EMAIL,
            cognito.OAuthScope.COGNITO_ADMIN,
            cognito.OAuthScope.OPENID,
            cognito.OAuthScope.PROFILE,
          ],
          callbackUrls: ["https://example.com"],
        },
      }
    );
  }
}
