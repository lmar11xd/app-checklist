package com.smartory.db;

import android.content.Context;

public class SQLTables {
    public void Delete(Context context){
        SqliteClass.getInstance(context).databasehelp.itemSql.deleteItemTable();
        SqliteClass.getInstance(context).databasehelp.levObsSql.deleteLevObsTable();
        SqliteClass.getInstance(context).databasehelp.checkListSql.deleteCheckListTable();
        SqliteClass.getInstance(context).databasehelp.tranportUnitSql.deleteUnitTable();
        SqliteClass.getInstance(context).databasehelp.operacionSql.deleteOperationTable();
        SqliteClass.getInstance(context).databasehelp.driverSql.deleteDriverTable();
        SqliteClass.getInstance(context).databasehelp.inspectorSql.deleteInspectoTable();
        SqliteClass.getInstance(context).databasehelp.supervisorSql.deleteSupervisorSqlTable();
        SqliteClass.getInstance(context).databasehelp.locationSql.deleteLocationTable();
        SqliteClass.getInstance(context).databasehelp.carrierSql.deleteCarrierSqlTable();
        SqliteClass.getInstance(context).databasehelp.routeSql.deleteRouteTable();
    }
}
