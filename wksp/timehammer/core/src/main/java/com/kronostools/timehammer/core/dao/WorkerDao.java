package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.core.constants.WorkerProfile;
import com.kronostools.timehammer.core.model.InsertResult;
import com.kronostools.timehammer.core.model.InsertResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class WorkerDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerDao.class);

    private final PgPool client;

    public WorkerDao(final PgPool client) {
        this.client = client;
    }

    public Uni<InsertResult> newWorker(final String registrationId, final String fullname, final String chatId, final RegistrationRequestForm registrationRequestForm) {
        return client.begin()
                .flatMap(tx ->
                    tx.preparedQuery(
                        "INSERT INTO worker(internal_id, full_name, profile) " +
                            "VALUES ($1, $2, $3) " +
                            "ON CONFLICT DO NOTHING")
                        .execute(Tuple.of(registrationId, fullname, WorkerProfile.WORKER.name()))
                            .onItem().produceUni(rs -> tx.preparedQuery(
                                        "INSERT INTO worker_chat(worker_internal_id, chat_id) " +
                                            "VALUES ($1, $2)")
                                        .execute(Tuple.of(registrationId, chatId)))
                            .onItem().produceUni(rs -> {
                                final List<Object> preferencesParams = new ArrayList<>() {{
                                    add(registrationId);
                                    add(registrationRequestForm.getWorkerExternalId());
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getWorkEndMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchStartMon()));
                                    add(CommonDateTimeUtils.parseTimeFromForm(registrationRequestForm.getDefaultTimetable().getLunchEndMon()));
                                    add(registrationRequestForm.getCompanyCode());
                                    add(registrationRequestForm.getWorkSsid());
                                    add(registrationRequestForm.getWorkCity());
                                }};

                                return tx.preparedQuery(
                                        "INSERT INTO worker_preferences(" +
                                            "    worker_internal_id, worker_external_id, " +
                                            "    work_start_mon, work_end_mon, lunch_start_mon, lunch_end_mon, " +
                                            "    work_start_tue, work_end_tue, lunch_start_tue, lunch_end_tue, " +
                                            "    work_start_wed, work_end_wed, lunch_start_wed, lunch_end_wed, " +
                                            "    work_start_thu, work_end_thu, lunch_start_thu, lunch_end_thu, " +
                                            "    work_start_fri, work_end_fri, lunch_start_fri, lunch_end_fri, " +
                                            "    company_code, work_ssid, work_city_code" +
                                            ") " +
                                            "VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21, $22, $23, $24, $25) " +
                                            "ON CONFLICT (worker_internal_id) DO UPDATE SET " +
                                                "worker_external_id = $2, " +
                                                "work_start_mon = $3, work_end_mon = $4, lunch_start_mon = $5, lunch_end_mon = $6, " +
                                                "work_start_tue = $7, work_end_tue = $8, lunch_start_tue = $9, lunch_end_tue = $10, " +
                                                "work_start_wed = $11, work_end_wed = $12, lunch_start_wed = $13, lunch_end_wed = $14, " +
                                                "work_start_thu = $15, work_end_thu = $16, lunch_start_thu = $17, lunch_end_thu = $18, " +
                                                "work_start_fri = $19, work_end_fri = $20, lunch_start_fri = $21, lunch_end_fri = $22, " +
                                                "company_code = $23, work_ssid = $24, work_city_code = $25")
                                        .execute(Tuple.wrap(preferencesParams));
                            })
                            // on success, commit
                            .onItem()
                                .produceUni(x -> tx.commit())
                            // on failure rollback
                            .onFailure()
                                .recoverWithUni(e -> {
                                    final String errorMessage = stringFormat("Unexpected error while registering worker '{}'", registrationId);
                                    LOG.error(errorMessage, e);

                                    return tx.rollback();
                                })
            )
            .flatMap(v -> {
                LOG.debug("Registered new worker '{}' with chat '{}' and its preferences", registrationId, chatId);
                return Uni.createFrom().item(new InsertResultBuilder().build());
            })
            .onFailure()
                .recoverWithItem(e -> new InsertResultBuilder().errorMessage("Worker could not be registered").build());
    }
}