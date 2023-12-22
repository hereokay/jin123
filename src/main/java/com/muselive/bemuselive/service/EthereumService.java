package com.muselive.bemuselive.service;

import com.muselive.bemuselive.contract.InhaKrw;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

@Service
public class EthereumService {

    @Value("${ethereum.rpc-url}")
    private String ethereumRpcUrl;

    @Value("${owner.private-key}")
    private String PRIVATE_KEY;



    private Web3j web3j;
    private Credentials credentials;
    private InhaKrw contract;
    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService(ethereumRpcUrl));
        this.credentials = Credentials.create(PRIVATE_KEY);
        this.contract = InhaKrw.load(
                "0xa17c5dD460CA3A24607615c677f44891eA88758C",web3j, credentials, GAS_PRICE, GAS_LIMIT);
    }

    public BigInteger getBlockNumber() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        return blockNumber.getBlockNumber();
    }

    public TransactionReceipt reqMint(int school_id, int amount) throws Exception {

        //TODO
        // 공개주소 to 조회

        return contract.mint(to, BigInteger.valueOf(amount)).send();
    }

    public TransactionReceipt reqPay(int school_id, int service_id, Integer amount) throws Exception {

        //TODO
        // from 주소 , to주소 조회

        return contract.transferFrom(from,to, BigInteger.valueOf(amount)).send();
    }

}