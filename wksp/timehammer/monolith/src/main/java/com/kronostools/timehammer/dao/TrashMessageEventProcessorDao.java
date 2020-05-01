package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.enums.TrashMessageStatus;
import com.kronostools.timehammer.model.TrashMessage;
import com.kronostools.timehammer.model.TrashMessageId;
import com.kronostools.timehammer.utils.Constants.Buses;
import com.kronostools.timehammer.vo.TrashMessageVo;
import io.quarkus.vertx.ConsumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class TrashMessageEventProcessorDao {
    private static final Logger LOG = LoggerFactory.getLogger(TrashMessageEventProcessorDao.class);

    private final TrashMessageDao trashMessageDao;

    public TrashMessageEventProcessorDao(final TrashMessageDao trashMessageDao) {
        this.trashMessageDao = trashMessageDao;
    }

    @ConsumeEvent(value = Buses.ADD_TRASH_MESSAGE, blocking = true)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addTrashMessageProcessor(final TrashMessageVo trashMessageVo) {
        LOG.debug("BEGIN addTrashMessageProcessor: [{}]", trashMessageVo);

        TrashMessageId trashMessageId = new TrashMessageId();
        trashMessageId.setChatId(trashMessageVo.getChatId());
        trashMessageId.setTimestamp(trashMessageVo.getTimestamp());

        TrashMessage trashMessage = new TrashMessage();
        trashMessage.setId(trashMessageId);
        trashMessage.setStatus(TrashMessageStatus.UNCHECKED);
        trashMessage.setText(trashMessageVo.getText());

        trashMessageDao.save(trashMessage);

        LOG.debug("END addTrashMessageProcessor");
    }
}