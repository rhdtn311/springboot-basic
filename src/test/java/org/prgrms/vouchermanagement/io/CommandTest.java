package org.prgrms.vouchermanagement.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.vouchermanagement.exception.command.InCorrectCommandException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandTest {

    @Test
    @DisplayName("입력값이 유효한 값인 경우 Command 반환 확인")
    void correctInputMatch() {
        // given
        String noneInput = "none";
        String exitInput = "exit";
        String createInput = "create";
        String listInput = "list";
        String blackListInput = "blacklist";
        String createCustomerInput = "create customer";
        String customerListInput = "show customers";

        // when
        Command noneCommand = Command.findCommand(noneInput);
        Command exitCommand = Command.findCommand(exitInput);
        Command createCommand = Command.findCommand(createInput);
        Command listCommand = Command.findCommand(listInput);
        Command blackListCommand = Command.findCommand(blackListInput);
        Command createCustomerCommand = Command.findCommand(createCustomerInput);
        Command customerListCommand = Command.findCommand(customerListInput);

        // then
        assertThat(noneCommand).isEqualTo(Command.NONE);
        assertThat(exitCommand).isEqualTo(Command.EXIT);
        assertThat(createCommand).isEqualTo(Command.CREATE);
        assertThat(listCommand).isEqualTo(Command.LIST);
        assertThat(blackListCommand).isEqualTo(Command.BLACKLIST);
        assertThat(createCustomerCommand).isEqualTo(Command.CREATE_CUSTOMER);
        assertThat(customerListCommand).isEqualTo(Command.CUSTOMER_LIST);
    }

    @Test
    @DisplayName("입력값이 유효하지 않는 값인 경우 예외 확인")
    void abnormalInputNotMatch() {
        // given
        String abnormalInput = "";

        // when
        assertThrows(InCorrectCommandException.class, () -> Command.findCommand(abnormalInput));
    }
}