/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2014 ForgeRock AS. All Rights Reserved
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://forgerock.org/license/CDDLv1.0.html
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at http://forgerock.org/license/CDDLv1.0.html
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * @author Gael Allioux <gael.allioux@forgerock.com>
 */


import groovy.sql.Sql
import org.forgerock.openicf.connectors.scriptedsql.ScriptedSQLConfiguration
import org.forgerock.openicf.misc.scriptedcommon.OperationType
import org.identityconnectors.common.logging.Log
import org.identityconnectors.framework.common.objects.ObjectClass
import org.identityconnectors.framework.common.objects.OperationOptions
import org.identityconnectors.framework.common.objects.Uid

import java.sql.Connection

def action = action as OperationType
def configuration = configuration as ScriptedSQLConfiguration
def connection = connection as Connection
def log = log as Log
def objectClass = objectClass as ObjectClass
def options = options as OperationOptions
def uid = uid as Uid
def ORG = new ObjectClass("organization")

// Parameters:
// The connector sends the following:
// connection: handler to the SQL connection
// configuration : handler to the connector's configuration object
// action: a string describing the action ("DELETE" here)
// log: a handler to the Log facility
// objectClass: a String describing the Object class (__ACCOUNT__ / __GROUP__ / other)
// options: a handler to the OperationOptions Map
// uid: String for the unique id (__UID__)that specifies the object to delete
// RETURNS: Nothing

log.info("Entering " + action + " Script");
def sql = new Sql(connection);

assert uid != null

switch (objectClass) {
    case ObjectClass.ACCOUNT:
        sql.execute("DELETE FROM Users where id= ?", [uid.uidValue])
        break

    case ObjectClass.GROUP:
        sql.execute("DELETE FROM Groups where id= ?", [uid.uidValue])
        break

    case ORG:
        sql.execute("DELETE FROM Organizations where id= ?", [uid.uidValue])
        break

    default:
        throw UnsupportedOperationException(action.name() + " operation of type:" + objectClass)
}