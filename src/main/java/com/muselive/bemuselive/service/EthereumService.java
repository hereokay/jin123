package com.muselive.bemuselive.service;

import com.muselive.bemuselive.VO.ServiceDTO;
import com.muselive.bemuselive.VO.User;
import com.muselive.bemuselive.contract.InhaKrw;
import com.muselive.bemuselive.mapper.ServiceMapper;
import com.muselive.bemuselive.mapper.UserMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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
    static private String ethereumRpcUrl;

    @Value("${owner.private-key}")
    static private String PRIVATE_KEY;

    @Value("${owner.tel-token}")
    static private String botToken;



    @Autowired
    UserMapper userMapper;

    @Autowired
    ServiceMapper serviceMapper;
    private final BigInteger amountInWei = new BigInteger("1000000000000000000"); // 1 ETH


    private Web3j web3j;
    private Credentials credentials;
    private final long chainId = 80001;
    private final String tokenAddress = "0xbF603CDb8367bd6600df47E659209fA57682d6D5";
    private InhaKrw contract;
    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService(ethereumRpcUrl));
        this.credentials = Credentials.create(PRIVATE_KEY);

        TransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);

        this.contract = InhaKrw.load(
                tokenAddress,
                web3j,
                txManager,
                new DefaultGasProvider()
                );
    }


    public static Boolean sendMessageTelegram(String msg) {
        Long chatId = 5297812588L;

        // TelegramBot 객체 생성
        TelegramBot bot = new TelegramBot(botToken);

        // 메시지 보내기
        SendResponse response = bot.execute(new SendMessage(chatId, msg));
        return response.isOk();
    }

    public TransactionReceipt reqMint(int school_id, int amount) throws Exception {

        User user = null;
        Map userinfo = new HashMap<>();
        userinfo.put("school_id",school_id);
        user = userMapper.getUserInfo(userinfo);
        String to = user.getWallet_address();


        TransactionReceipt receipt = contract.mint(to,
                BigInteger.valueOf(amount).multiply(amountInWei)).send();

        if (!receipt.isStatusOK()){
            sendMessageTelegram(receipt.getRevertReason());
        }
        return receipt;
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
        String pk = user.private_key;
        String from = user.getWallet_address();

        Credentials userCredentials = Credentials.create(pk);
        TransactionManager txManager = new RawTransactionManager(web3j, userCredentials, chainId);

        InhaKrw userContract = InhaKrw.load(
                tokenAddress,
                web3j,
                txManager,
                amountInWei.divide(new BigInteger(String.valueOf(1000000000))).multiply(new BigInteger(String.valueOf(30))),
                new BigInteger(String.valueOf(150000))
        );

        TransactionReceipt receipt = userContract.transfer(to, BigInteger.valueOf(amount).multiply(amountInWei)).send();

        if (!receipt.isStatusOK()){
            sendMessageTelegram(receipt.getRevertReason());
        }
        return receipt;

    }

}