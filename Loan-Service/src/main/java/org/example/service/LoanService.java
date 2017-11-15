/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.service;

import org.example.service.model.Loan;
import org.example.service.model.Payload;
import org.example.service.util.DBUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 1.0.0
 */
@Path("/loan-service")
public class LoanService {

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Loan> get(@PathParam("customerId") int customerId) {
        System.out.println("GET invoked");
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            String sql = "select id, loanAmount, approvedBy, dueDate from Loan where customerId=" + customerId;
            System.out.println(sql);
            ResultSet resultSet = con.createStatement().executeQuery(sql);
            ArrayList<Loan> loanList = new ArrayList<>();
            while (resultSet.next()) {
                Loan loan = new Loan();
                loan.setId(resultSet.getInt("id"));
                loan.setLoanAmount(resultSet.getDouble("loanAmount"));
                loan.setApprovedBy(resultSet.getString("approvedBy"));
                loan.setDueDate(resultSet.getDate("dueDate").toString());
                loan.setCustomerId(customerId);
                loanList.add(loan);
            }
            return loanList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
        }
        return null;
    }

    @GET
    @Path("/getRiskLoans")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Loan> getRiskLoans(@QueryParam("customerId") int customerId, @QueryParam("creditLimit") double
            creditLimit) {
        Connection con = null;
        try {
            String sql = "select id, loanAmount, approvedBy, dueDate from Loan where customerId=" + customerId +
                    " and loanAmount > " + creditLimit;
            System.out.println(sql);
            con = DBUtil.getConnection();
            ResultSet resultSet = con.createStatement().executeQuery(sql);
            ArrayList<Loan> loanList = new ArrayList<>();
            while (resultSet.next()) {
                Loan loan = new Loan();
                loan.setId(resultSet.getInt("id"));
                loan.setLoanAmount(resultSet.getDouble("loanAmount"));
                loan.setApprovedBy(resultSet.getString("approvedBy"));
                loan.setDueDate(resultSet.getDate("dueDate").toString());
                loan.setCustomerId(customerId);
                loanList.add(loan);
            }
            return loanList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
        }
        return null;
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String post(Payload payload) {
        System.out.println("POST invoked");
        Loan loan = payload.getLoan();
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            String sql = "insert into Loan (loanAmount, approvedBy, dueDate, customerId) values (" + loan
                    .getLoanAmount() + ", '" + loan.getApprovedBy() + "', '" + loan.getDueDate() + "', " +
                    loan.getCustomerId() + ")";
            System.out.println(sql);
            con.createStatement().execute(sql);
            return "{\"status\":\"success\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"Database error occurred. " + e.getMessage() + "\"}";
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String put(@PathParam("id") int id, Payload payload) {
        System.out.println("PUT invoked");
        Loan loan = payload.getLoan();
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            String sql = "update Loan set loanAmount=" + loan.getLoanAmount() + ", approvedBy='" + loan
                    .getApprovedBy() + "', dueDate='" + loan.getDueDate().toString() + "' where id=" + id;
            System.out.println(sql);
            con.createStatement().execute(sql);
            return "{\"status\":\"success\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"Database error occurred. " + e.getMessage() + "\"}";
        } finally {
            DBUtil.closeConnection(con);
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") int id) {
        System.out.println("DELETE invoked");
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            String sql = "delete from Loan where id=" + id;
            System.out.println(sql);
            con.createStatement().execute(sql);
            return "{\"status\":\"success\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"database error occurred. " + e.getMessage() + "\"}";
        } finally {
            DBUtil.closeConnection(con);
        }
    }
}
