package com.example.upibudgettracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.example.upibudgettracker.SMSParser;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus != null) {
            for (Object pdu : pdus) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                String msgBody = sms.getMessageBody();

//                float amount = SMSParser.extractAmount(msgBody);
//                if (amount > 0) {
//                    BudgetManager.updateSpending(context, amount);
//                    SharedPreferences prefs = context.getSharedPreferences("BudgetPrefs", Context.MODE_PRIVATE);
//                    String prevLogs = prefs.getString("smsLogs", "");
//                    prefs.edit().putString("smsLogs", prevLogs + "\n" + msgBody).apply();
//                }
            }
        }
    }
}