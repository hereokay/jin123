package com.muselive.bemuselive.service;

import com.muselive.bemuselive.VO.ServiceDTO;
import com.muselive.bemuselive.VO.User;
import com.muselive.bemuselive.contract.InhaKrw;
import com.muselive.bemuselive.mapper.ServiceMapper;
import com.muselive.bemuselive.mapper.UserMapper;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

@Service
public class EthereumService {

    @Value("${ethereum.rpc-url}")
    private String ethereumRpcUrl;

    @Value("${owner.private-key}")
    private String PRIVATE_KEY;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ServiceMapper serviceMapper;
    private final BigInteger amountInWei = new BigInteger("1000000000000000000"); // 1 ETH


    private Web3j web3j;
    private Credentials credentials;

    private InhaKrw contract;
    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService(ethereumRpcUrl));
        this.credentials = Credentials.create(PRIVATE_KEY);

        long chainId = 80001;
        String tokenAddress = "0xbF603CDb8367bd6600df47E659209fA57682d6D5";
        TransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);

        this.contract = InhaKrw.load(
                tokenAddress,
                web3j,
                txManager,
                new DefaultGasProvider()
                );
    }

    public BigInteger getBlockNumber() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        return blockNumber.getBlockNumber();
    }

    public TransactionReceipt reqMint(int school_id, int amount) throws Exception {




        User user = null;

        Map userinfo = new HashMap<>();
        userinfo.put("school_id",school_id);


        user = userMapper.getUserInfo(userinfo);

        String to = user.getWallet_address();


        return contract.mint(to,
                BigInteger.valueOf(amount).multiply(amountInWei)).send();
    }

    public TransactionReceipt reqPayment(int school_id, int service_id, Integer amount) throws Exception {


        User user = null;
        ServiceDTO serviceDTO = null;

        Map paymentinfo = new HashMap<>();
        paymentinfo.put("school_id",school_id);
        paymentinfo.put("service_id",service_id);


        user = userMapper.getUserInfo(paymentinfo);
        serviceDTO = serviceMapper.getServiceInfo(paymentinfo);

        String to = serviceDTO.getWallet_address();
        String from = user.getWallet_address();


        return contract.transferFrom(from, to, BigInteger.valueOf(amount).multiply(amountInWei)).send();

    }

}