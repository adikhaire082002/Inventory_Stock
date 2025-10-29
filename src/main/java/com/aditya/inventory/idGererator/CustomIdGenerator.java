package com.aditya.inventory.idGererator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String tableName= object.getClass().getSimpleName();

        String prefix ;
        switch (tableName){
            case "Admin":{
                prefix = "ADM";
                break;
            }
            case "User":{
                prefix= "USR";
                break;
            }
            case "Dealer":{
                prefix= "DLR";
                break;
            }
            case "Customer":{
                prefix=  "CMR";
                break;
            }
            default:{
                prefix= "ID";
            }
        }
        Object result = null;

        try {
          result =  session.createNativeQuery("SELECT last_number FROM id_sequence WHERE table_name = :name")
                    .setParameter("name",tableName)
                    .uniqueResult();

        } catch (Exception e) {

        }
        long nextdId ;
        if (result==null){
            nextdId = 1;
            session.createNativeQuery("INSERT INTO id_sequence(table_name,last_number) VALUES (:name,:nextdId) ")
                    .setParameter("name",tableName)
                    .setParameter("nextdId",nextdId)
                    .executeUpdate();
        }else{
            nextdId = ((Number)result).byteValue()+1;

            session.createNativeQuery("UPDATE id_sequence SET last_number=:nextdId WHERE table_name=:name")
                    .setParameter("name",tableName)
                    .setParameter("nextdId",nextdId)
                    .executeUpdate();
        }
        return prefix + nextdId;
    }
}
