package uk.me.eastmans.multi.em.invoices;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class InvoiceService {
    final public  InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
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
    }

    @Transactional
    public void saveOrCreate(Invoice invoice) {
        invoiceRepository.saveAndFlush(invoice);
    }

}