package com.smartory.network;

import static com.smartory.resource.HorizontalValues.LABEL_OBSERVATION_ITEM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.smartory.db.SqliteClass;
import com.smartory.models.CheckListModel;
import com.smartory.models.GetCheckListModel;
import com.smartory.models.GetLevObsModel;
import com.smartory.models.ImageModel;
import com.smartory.models.ItemCheckListModel;
import com.smartory.models.LevObsModel;
import com.smartory.models.OperacionModel;
import com.smartory.models.ResponseImageItemChecklist;
import com.smartory.models.SyncBDLocal;
import com.smartory.models.operations.CarrierByOperation;
import com.smartory.models.operations.DriverByOperation;
import com.smartory.models.operations.InspectorByOperation;
import com.smartory.models.operations.Route;
import com.smartory.models.operations.SupervisorByOperation;
import com.smartory.models.operations.TransportUnitByOperation;
import com.smartory.utils.Util;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

public class ReloadData {
    @SuppressLint("StaticFieldLeak")
    private static SqliteClass.DatabaseHelper SQL;
    UpdateTaskImage updateTaskImage;
    UpdateTaskOperation updateTaskOperation;
    UpdateTaskCarriers updateTaskCarriers;
    UpdateTaskSupervisor updateTaskSupervisor;
    UpdateTaskRoute updateTaskRoute;
    UpdateTaskInspector updateTaskInspector;
    UpdateTaskTransportUnit updateTaskTransportUnit;
    UpdateTaskDrivers updateTaskDrivers;
    UpdateTaskObservations updateTaskObservations;
    UpdateTaskSectionTemplate updateTaskSectionTemplate;
    UpdateTaskChecklist updateTaskChecklist;
    SyncBDLocal syncBDLocal;
    static boolean failedReload = false;

    public ReloadData(Context context, InterfaceAPI api) {
        SharedPreferences sharedPrefLogin = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        syncBDLocal = new SyncBDLocal(api, sharedPrefLogin.getString("token", "default"), context);
        SQL = SqliteClass.getInstance(context).databasehelp;
        updateTaskImage = new UpdateTaskImage();
        updateTaskOperation = new UpdateTaskOperation();
        updateTaskCarriers = new UpdateTaskCarriers();
        updateTaskSupervisor = new UpdateTaskSupervisor();
        updateTaskRoute = new UpdateTaskRoute();
        updateTaskInspector = new UpdateTaskInspector();
        updateTaskTransportUnit = new UpdateTaskTransportUnit();
        updateTaskDrivers = new UpdateTaskDrivers();
        updateTaskObservations = new UpdateTaskObservations();
        updateTaskSectionTemplate = new UpdateTaskSectionTemplate();
        updateTaskChecklist = new UpdateTaskChecklist();
    }

    public void startReloadAll(String datetime_sync) {
        if (datetime_sync != null)
            syncBDLocal.setParams(datetime_sync);
        updateTaskImage.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskOperation.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskCarriers.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskSupervisor.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskRoute.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskInspector.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskTransportUnit.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskDrivers.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskObservations.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskSectionTemplate.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
        updateTaskChecklist.executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>()), syncBDLocal);
    }

    public boolean isFinish() {
        return updateTaskImage.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskOperation.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskCarriers.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskSupervisor.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskRoute.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskInspector.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskTransportUnit.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskDrivers.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskObservations.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskSectionTemplate.getStatus() == AsyncTask.Status.FINISHED &&
                updateTaskChecklist.getStatus() == AsyncTask.Status.FINISHED;
    }

    public static class UpdateTaskImage extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<ResponseImageItemChecklist>> getItemImage = syncBDLocal.getApi().getItemImageCheckList(syncBDLocal.getToken()).execute();
                Response<List<ResponseImageItemChecklist>> getItemImage2 = syncBDLocal.getApi().getItemImageLiftingMobile(syncBDLocal.getToken()).execute();
                if (getItemImage.isSuccessful()) {
                    if (getItemImage.body() != null) {
                        //SQL.imageSql.addImages(getItemImage.body(), 1);
                        for (ResponseImageItemChecklist checklistItem : getItemImage.body()) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setDocumentIdApi(checklistItem.getChecklist_section_item() + "");
                            imageModel.setImage_url(checklistItem.getImage());
                            imageModel.setComment(checklistItem.getComment());
                            imageModel.setLabel(checklistItem.getLabel());
                            imageModel.setState("server");
                            SQL.imageSql.addImage(imageModel);
                        }
                    }
                } else {
                    result = "error";
                }
                if (getItemImage2.isSuccessful()) {
                    if (getItemImage2.body() != null) {
                        //SQL.imageSql.addImages(getItemImage2.body(), 2);
                        for (ResponseImageItemChecklist liftingImageItem : getItemImage2.body()) {
                            ImageModel imageModel = new ImageModel();
                            imageModel.setDocumentIdApi(liftingImageItem.getNon_compliance() + "");
                            imageModel.setImage_url(liftingImageItem.getImage());
                            imageModel.setComment("");
                            imageModel.setLabel(LABEL_OBSERVATION_ITEM);
                            imageModel.setState("server");
                            imageModel.setType(liftingImageItem.getState_lifting());
                            SQL.imageSql.addImage(imageModel);
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskOperation extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<OperacionModel>> responseGetOperations = syncBDLocal.getApi().getOperations(syncBDLocal.getToken(), "").execute();
                if (responseGetOperations.isSuccessful()) {
                    List<OperacionModel> operations = responseGetOperations.body();
                    if (operations != null) {
                        for (OperacionModel operation : operations) {
                            SQL.operacionSql.addOperation(
                                    String.valueOf(operation.getPk()),
                                    String.valueOf(operation.getName())
                            );
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskCarriers extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<CarrierByOperation>> getCarriers = syncBDLocal.getApi().getCarriers(syncBDLocal.getToken(), "").execute();
                if (getCarriers.isSuccessful() && getCarriers.body() != null) {
                    //List<CarrierByOperation> list = getCarriers.body();
                    for (CarrierByOperation carrier : getCarriers.body()) {
                        if (carrier.getCompany() != null || carrier.getOperation() != null) {
                            SQL.carrierSql.addCarrierSql(
                                    String.valueOf(carrier.getPk()),
                                    String.valueOf(carrier.getCarrier().getName()).toUpperCase(),
                                    String.valueOf(carrier.getCarrier().getPk()),
                                    String.valueOf(carrier.getOperation().getPk())
                            );
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskSupervisor extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<SupervisorByOperation>> getSupervisors = syncBDLocal.getApi().getSupervisors(syncBDLocal.getToken(), "").execute();
                if (getSupervisors.isSuccessful() && getSupervisors.body() != null) {
                    //List<SupervisorByOperation> list = getSupervisors.body();
                    for (SupervisorByOperation supervisor : getSupervisors.body()) {
                        if (supervisor.getCompany() != null || supervisor.getOperation() != null) {
                            SQL.supervisorSql.addSupervisorSql(
                                    String.valueOf(supervisor.getPk()),
                                    String.valueOf(supervisor.getSupervisor().getName()).toUpperCase(),
                                    String.valueOf(supervisor.getCompany().getPk()),
                                    String.valueOf(supervisor.getOperation().getPk())
                            );
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskRoute extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<Route>> getRoutes = syncBDLocal.getApi().getRoute(syncBDLocal.getToken()).execute();
                if (getRoutes.isSuccessful()) {
                    if (getRoutes.body() != null) {
                        for (Route route : getRoutes.body()) {
                            if (route.getOperation() != null) {
                                SQL.routeSql.addRouteSql(
                                        String.valueOf(route.getPk()),
                                        String.valueOf(route.getName()).toUpperCase(),
                                        String.valueOf(route.getOperation().getPk())
                                );
                            }
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskInspector extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<InspectorByOperation>> getInspectors = syncBDLocal.getApi().getInspectors(syncBDLocal.getToken(), "").execute();
                if (getInspectors.isSuccessful()) {
                    if (getInspectors.body() != null) {
                        for (InspectorByOperation inspector : getInspectors.body()) {
                            if (inspector.getCompany() != null || inspector.getOperation() != null) {
                                SQL.inspectorSql.addInspectorSql(
                                        String.valueOf(inspector.getPk()),
                                        String.valueOf(inspector.getInspector().getName()).toUpperCase(),
                                        String.valueOf(inspector.getOperation().getPk()),
                                        String.valueOf(inspector.getCompany().getPk()),
                                        String.valueOf(inspector.getCompany().getName())
                                );
                            }
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskTransportUnit extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                String date_updated_gte = "";
                Response<List<TransportUnitByOperation>> getTransportUnit = syncBDLocal.getApi().getTranportUnit(syncBDLocal.getToken(), date_updated_gte).execute();
                if (getTransportUnit.isSuccessful()) {
                    if (getTransportUnit.body() != null) {
                        for (TransportUnitByOperation transportUnit : getTransportUnit.body()) {
                            transportUnit.transport_unit.setPlate_cistern(transportUnit.transport_unit.getCisterns().get(0).getPlate_cisterns());
                            transportUnit.transport_unit.setPlate_tracto(transportUnit.transport_unit.getTractos().get(0).getPlate_tractos());
                            transportUnit.transport_unit.setPk_company(String.valueOf(transportUnit.transport_unit.getCompany().getPk()));
                            transportUnit.transport_unit.setPk_operation(String.valueOf(transportUnit.operation.pk));
                            transportUnit.transport_unit.id = transportUnit.pk;
                            SQL.tranportUnitSql.addUnit(transportUnit.transport_unit);
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskDrivers extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<DriverByOperation>> getDrivers = syncBDLocal.getApi().getDriver(syncBDLocal.getToken(), "").execute();
                if (getDrivers.isSuccessful()) {
                    if (getDrivers.body() != null) {
                        for (DriverByOperation driver : getDrivers.body()) {
                            if (driver.getCompany() != null || driver.getOperation() != null) {
                                SQL.driverSql.addDriverSql(
                                        String.valueOf(driver.getPk()),
                                        String.valueOf(driver.getDriver().getName()).toUpperCase(),
                                        String.valueOf(driver.getCompany().getPk()),
                                        String.valueOf(driver.getOperation().getPk())
                                );
                            }
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskObservations extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<GetLevObsModel>> getLevObsItems = syncBDLocal.getApi().getLevObs(syncBDLocal.getToken()).execute();
                if (getLevObsItems.isSuccessful()) {
                    if (getLevObsItems.body() != null) {
                        for (GetLevObsModel observationItem : getLevObsItems.body()) {
                            LevObsModel levObsModel = new LevObsModel();
                            levObsModel.setPk(observationItem.getPk());
                            levObsModel.setId_item(observationItem.item.getId());
                            levObsModel.setCheck_list(observationItem.item.getCheck_list());
                            levObsModel.setCheck_list_date(observationItem.item.getCheck_list_date());
                            levObsModel.setTransport_company(observationItem.item.getTransport_company());
                            levObsModel.setTransport_unit(observationItem.item.getTransport_unit());
                            levObsModel.setSection(observationItem.item.getSection());
                            levObsModel.setCheck_list_item(observationItem.item.getCheck_list_item());
                            levObsModel.setSeverity(observationItem.item.getSeverity());
                            levObsModel.setObservation_description(observationItem.item.getObservation_description());
                            levObsModel.setObservation_lifting(observationItem.item.getObservation_lifting());
                            levObsModel.setDue_date(observationItem.item.getDue_date());
                            levObsModel.setState(observationItem.getState());
                            SQL.levObsSql.addLevObs(levObsModel);
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskSectionTemplate extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<ItemCheckListModel>> getItemCheck = syncBDLocal.getApi().getCheckListItem(syncBDLocal.getToken(), "").execute();
                if (getItemCheck.isSuccessful()) {
                    if (getItemCheck.body() != null) {
                        for (ItemCheckListModel checklistItem : getItemCheck.body()) {
                            ItemCheckListModel itemModel = new ItemCheckListModel();
                            itemModel.setId(checklistItem.getId());
                            itemModel.setName(checklistItem.getName());
                            itemModel.setId_section(String.valueOf(checklistItem.getCheck_list_section().getId()));
                            itemModel.setId_check_list("plantilla");
                            itemModel.setCompliance_image("");
                            itemModel.setNon_compliance_image("");
                            itemModel.setOperations(checklistItem.getOperations());
                            itemModel.setObservation_lifting_image("");
                            itemModel.setState("NA");
                            SQL.itemSql.addItem(itemModel);
                        }
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    public static class UpdateTaskChecklist extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                failedReload = true;
            }
            super.onPostExecute(result);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<GetCheckListModel>> getCheckLists = syncBDLocal.getApi().getCheckList(syncBDLocal.getToken(), Util.getLast30Date(), "", "").execute();
                if (getCheckLists.isSuccessful()) {
                    if (getCheckLists.body() != null) {
                        for (GetCheckListModel checkList : getCheckLists.body()) {
                            CheckListModel checkListModel = new CheckListModel();
                            checkListModel.setId(checkList.getId());
                            checkListModel.setNumber(checkList.getNumber());
                            checkListModel.setType_check(checkList.getType_check());
                            checkListModel.setDatetime(checkList.getDatetime());
                            if (checkList.company_shipment != null) {
                                checkListModel.setName_company_shipment(checkList.company_shipment.getName());
                                checkListModel.setCompany_shipment(checkList.company_shipment.getPk());
                            }
                            if (checkList.operation != null) {
                                checkListModel.setName_operation(checkList.operation.getName());
                                checkListModel.setOperation(checkList.operation.getPk());
                            }
                            if (checkList.inspector != null) {
                                checkListModel.setName_inspector(checkList.inspector.getParty());
                                checkListModel.setInspector(checkList.inspector.getPk());
                            }
                            if (checkList.supervisor != null) {
                                checkListModel.setName_supervisor(checkList.supervisor.name);
                                checkListModel.setSupervisor(checkList.supervisor.getPk());
                            }
                            if (checkList.driver != null) {
                                checkListModel.setName_driver(checkList.driver.getName_driver());
                                checkListModel.setDriver(checkList.driver.getId());
                            }
                            if (checkList.route != null) {
                                checkListModel.setName_route(checkList.route.getName());
                                checkListModel.setRoute(checkList.route.getPk());
                            }
                            if (checkList.location != null) {
                                checkListModel.setName_location(checkList.location.getName());
                                checkListModel.setLocation(checkList.location.getId());
                            }

                            String lastInspection = checkList.getLastInspection();
                            lastInspection = lastInspection.replace("T", " / ").replace("Z", "");

                            checkListModel.setPlate_tracto(checkList.transport_unit.getTractos().get(0));
                            checkListModel.setPlate_cistern(checkList.transport_unit.getCisterns().get(0));
                            checkListModel.setState(checkList.getState());
                            checkListModel.setMileage(checkList.getMileage());
                            checkListModel.setLastInspection(lastInspection);
                            checkListModel.setYearFabrication(checkList.getYearFabrication());
                            checkListModel.setProgress(checkList.getProgress());
                            String idCheckList = SQL.checkListSql.addcheckList(checkListModel);

                            new UpdateTaskItemsChecklist().executeOnExecutor(new ThreadPoolExecutor(50, Integer.MAX_VALUE, 1,
                                            TimeUnit.SECONDS, new LinkedBlockingQueue<>()),
                                    new SyncBDLocal(
                                            syncBDLocal.getApi(),
                                            syncBDLocal.getToken(),
                                            syncBDLocal.getContext(),
                                            String.valueOf(checkList.getId()),
                                            idCheckList
                                    )
                            );
                        }
                    }
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

    static public class UpdateTaskItemsChecklist extends AsyncTask<SyncBDLocal, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(SyncBDLocal... params) {
            String result = null;
            try {
                SyncBDLocal syncBDLocal = params[0];
                Response<List<ItemCheckListModel>> getCheckListItems =
                        syncBDLocal.getApi().getCheckListItemByCheckList(syncBDLocal.getToken(), syncBDLocal.getParams()[0], "").execute();
                if (getCheckListItems.isSuccessful()) {
                    if (getCheckListItems.body() != null) {
                        SQL.itemSql.addItems(getCheckListItems.body());
                        /*for (ItemCheckListModel checkListItem : getCheckListItems.body()) {
                            checkListItem.setId_check_list((String) syncBDLocal.getParams()[1]);
                            checkListItem.setId_section(String.valueOf(checkListItem.section.getId()));
                            SQL.itemSql.addItem(checkListItem);
                        }*/
                    }
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                result = "error";
                e.printStackTrace();
            }
            return result;
        }
    }

}
