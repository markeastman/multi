package uk.me.eastmans.multi.em.invoices;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.eastmans.multi.em.kafka.PostKafkaEvent;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class InvoiceService {
    final public  InvoiceRepository invoiceRepository;
    private final PostKafkaEvent postKafkaEvent;

    public InvoiceService(PostKafkaEvent postKafkaEvent, InvoiceRepository invoiceRepository) {
        this.postKafkaEvent = postKafkaEvent;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Optional<Invoice> getInvoice(long id) {
        return invoiceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Invoice> listAll() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.delete(invoice);
        postKafkaEvent.postDelete("Invoice", invoice.getId() );
    }

    @Transactional
    public void saveOrCreate(Invoice invoice) {
        boolean create = invoice.getId() == null;
        invoiceRepository.saveAndFlush(invoice);
        if (create) {
            postKafkaEvent.postCreate("Invoice", invoice.getId() );
        } else {
            postKafkaEvent.postUpdate("Invoice", invoice.getId() );
        }
    }

}