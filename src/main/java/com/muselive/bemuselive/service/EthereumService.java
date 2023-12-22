package com.muselive.bemuselive.service;

import com.muselive.bemuselive.contract.InhaKrw;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService(ethereumRpcUrl));
        this.credentials = Credentials.create(PRIVATE_KEY);
    }

    public BigInteger getBlockNumber() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        return blockNumber.getBlockNumber();
    }

    public TransactionReceipt reqMint(String to, Integer amount) throws Exception {
        InhaKrw contract = InhaKrw.load(
                "0xa17c5dD460CA3A24607615c677f44891eA88758C",web3j, credentials, GAS_PRICE, GAS_LIMIT);

        return contract.mint(to, BigInteger.valueOf(amount)).send();
    }



}