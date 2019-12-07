package payment.api.mapper;

import com.github.voteva.Operation;
import com.github.voteva.Service;
import payment.api.model.TransactionDto;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static payment.api.consts.Constants.DATE_FORMAT;

public class OperationMapper {
    public static Operation toOperation(
            String publisher,
            String initiator,
            String operationUid,
            String requestedService,
            Map<String, String> requestedData,
            List<String> requiredServices,
            TransactionDto transaction) {
        Map<String, String> paymentResponse = new HashMap<>();
        paymentResponse.put("userUid", transaction.getUserUid().toString());
        paymentResponse.put("providerUid", transaction.getUserUid().toString());
        paymentResponse.put("currency", transaction.getCurrency());
        paymentResponse.put("status", transaction.getStatus().name());
        paymentResponse.put("extendedStatus", transaction.getExtendedStatus().name());
        paymentResponse.put("date", new SimpleDateFormat(DATE_FORMAT).format(transaction.getDate()));
        paymentResponse.put("amount", transaction.getAmount().toString());
        paymentResponse.put("comment", transaction.getComment());
        paymentResponse.put("sourceId", transaction.getSourceUid().toString());
        paymentResponse.put("sourceName", transaction.getSourceName());

        Map<String, Service> paymentServiceInfo = new HashMap<>();
        paymentServiceInfo.put(
                "payment",
                Service.builder()
                        .response(paymentResponse)
                        .build()
        );

        if (requestedService != null) {
            paymentServiceInfo.put(
                    requestedService,
                    Service.builder()
                            .request(requestedData)
                            .build()
            );
        }

        return Operation.builder()
                .publisher(publisher)
                .initiator(initiator)
                .operationId(operationUid)
                .requiredServices(requiredServices)
                .userId(transaction.getUserUid().toString())
                .services(paymentServiceInfo)
                .build();
    }
}
