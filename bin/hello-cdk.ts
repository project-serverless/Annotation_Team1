#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "aws-cdk-lib";
import { Todo3DaysCdkStack } from "../lib/hello-cdk-stack";
import { getAccountUniqueName, getDevAccount } from "../lib/config/accounts";
import * as os from "os";

const app = new cdk.App();

let userName = "team1";
const devAccount = getDevAccount(userName);

if (devAccount !== undefined) {
  new Todo3DaysCdkStack(app, `${getAccountUniqueName(devAccount)}`, {
    env: devAccount,
    context: devAccount,
  });
}

app.synth();
