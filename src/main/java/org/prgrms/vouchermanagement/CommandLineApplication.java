package org.prgrms.vouchermanagement;

import org.prgrms.vouchermanagement.customer.domain.Customer;
import org.prgrms.vouchermanagement.customer.service.BlackListFindService;
import org.prgrms.vouchermanagement.customer.service.CustomerService;
import org.prgrms.vouchermanagement.io.Command;
import org.prgrms.vouchermanagement.io.Input;
import org.prgrms.vouchermanagement.io.Output;
import org.prgrms.vouchermanagement.voucher.domain.Voucher;
import org.prgrms.vouchermanagement.voucher.service.VoucherCreateService;
import org.prgrms.vouchermanagement.voucher.service.VoucherDeleteService;
import org.prgrms.vouchermanagement.voucher.service.VoucherFindService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class CommandLineApplication {

    private static final Logger logger = getLogger(CommandLineApplication.class);

    private final Input input;
    private final Output output;
    private final VoucherCreateService voucherCreateService;
    private final VoucherFindService voucherFindService;
    private final VoucherDeleteService voucherDeleteService;
    private final BlackListFindService blackListFindService;
    private final CustomerService customerService;

    public CommandLineApplication(Input input,
                                  Output output,
                                  VoucherCreateService voucherCreateService,
                                  VoucherFindService voucherFindService,
                                  VoucherDeleteService voucherDeleteService,
                                  BlackListFindService blackListFindService,
                                  CustomerService customerService) {
        this.input = input;
        this.output = output;
        this.voucherCreateService = voucherCreateService;
        this.voucherFindService = voucherFindService;
        this.voucherDeleteService = voucherDeleteService;
        this.blackListFindService = blackListFindService;
        this.customerService = customerService;
    }

    void run() {

        Command command = Command.NONE;
        while (command.isNotExit()) {
            try {
                output.printCommandNotices();
                command = Command.findCommand(input.receiveCommand());
                switch (command) {
                    case CREATE:
                        createVoucher();
                        break;
                    case LIST:
                        printVouchers();
                        break;
                    case CREATE_CUSTOMER:
                        createCustomer();
                        break;
                    case CUSTOMER_LIST:
                        printCustomers();
                        break;
                    case CUSTOMER_VOUCHER_LIST:
                        printCustomerVouchers();
                        break;
                    case DELETE_CUSTOMER_VOUCHER:
                        deleteCustomerVoucher();
                        break;
                    case SEARCH_VOUCHER_OWNER:
                        printVoucherOwner();
                        break;
                    case BLACKLIST:
                        printBlacklist();
                        break;
                    case EXIT:
                        exit(command);
                }
            } catch (RuntimeException e) {
                logger.error("[ERROR] {}", e.getMessage());
            }
        }
    }

    private void createVoucher() {
        String voucherTypeInput = input.receiveVoucherType();
        int voucherAmountInput = input.receiveDiscountAmount(voucherTypeInput);
        String customerEmail = input.receiveCustomerEmail();

        voucherCreateService.createVoucher(voucherTypeInput, voucherAmountInput, customerEmail);
        output.printVoucherCreateMessage();
    }

    private void printVouchers() {
        List<Voucher> vouchers = voucherFindService.findAllVouchers();
        output.printList(vouchers);
    }

    private void createCustomer() {
        String name = input.receiveCustomerName();
        String email = input.receiveCustomerEmail();

        customerService.save(name, email);
    }

    private void printCustomers() {
        List<Customer> customers = customerService.findAll();
        output.printList(customers);
    }

    private void printCustomerVouchers() {
        String email = input.receiveCustomerEmail();
        Customer customer = customerService.findByEmail(email);
        List<Voucher> vouchers = voucherFindService.findVouchersByCustomerId(customer.getCustomerId());
        output.printList(vouchers);
    }

    private void deleteCustomerVoucher() {
        String email = input.receiveCustomerEmail();
        Customer customer = customerService.findByEmail(email);
        voucherDeleteService.deleteVouchersByCustomerId(customer.getCustomerId());
        output.printDeleteVoucherMessage();
    }

    private void printBlacklist() {
        List<Customer> blackList = blackListFindService.findAllBlackList();
        output.printList(blackList);
    }

    private void printVoucherOwner() {
        UUID voucherId = UUID.fromString(input.receiveVoucherId());
        Voucher voucher = voucherFindService.findVoucherByVoucherId(voucherId);
        Customer customer = customerService.findById(voucher.getCustomerId());
        output.printList(List.of(customer));
    }

    private void exit(Command command) {
        command = Command.EXIT;
    }

}
