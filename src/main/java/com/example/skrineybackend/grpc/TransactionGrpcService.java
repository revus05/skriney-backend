package com.example.skrineybackend.grpc;

import com.example.grpc.AddTransactionRequest;
import com.example.grpc.AddTransactionResponse;
import com.example.grpc.TransactionServiceGrpc;
import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.service.TransactionService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;

@GrpcService
@RequiredArgsConstructor
public class TransactionGrpcService extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final TransactionService transactionService;

    @Override
    public void addTransaction(AddTransactionRequest request, StreamObserver<AddTransactionResponse> responseObserver) {
        Currency currency = Currency.valueOf(request.getCurrency());
        CreateTransactionRequestDTO dto = new CreateTransactionRequestDTO(new BigDecimal(request.getAmount()), currency, null, "9d2c778d-30bf-44ed-94f5-fca3f281f3ee", "cbce068c-d552-4bf9-96b0-baa2c5369ad0");

        TransactionDTO transaction = transactionService.createTransaction(dto, "e889be6a-6356-43dc-8cce-d25a6920e69e");

        AddTransactionResponse response = AddTransactionResponse.newBuilder().setTransactionUuid(transaction.getUuid()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
