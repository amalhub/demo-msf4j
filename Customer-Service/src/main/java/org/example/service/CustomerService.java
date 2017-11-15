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

import org.example.service.model.Customer;
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
@Path("/customer-service")
public class CustomerService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Customer> get() {
        System.out.println("GET invoked");
        Connection con;
        try {
            con = DBUtil.getConnection();
            String sql = "select id, firstName, lastName, email, address, creditLimit from Customer";
            System.out.println(sql);
            ResultSet resultSet = con.createStatement().executeQuery(sql);
            ArrayList<Customer> customerList = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setFirstName(resultSet.getString("firstName"));
                customer.setLastName(resultSet.getString("LastName"));
                customer.setEmail(resultSet.getString("email"));
                customer.setAddress(resultSet.getString("address"));
                customer.setCreditLimit(resultSet.getDouble("creditLimit"));
                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection();
        }
        return null;
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String post(Payload payload) {
        System.out.println("POST invoked");
        Customer customer = payload.getCustomer();
        Connection con;
        try {
            con = DBUtil.getConnection();
            String sql = "insert into Customer (firstName, lastName, email, address, creditLimit) values ('" + customer
                    .getFirstName() + "', '" + customer.getLastName() + "', '" + customer.getEmail() + "', '" +
                    customer.getAddress() + "', " + customer.getCreditLimit() + ")";
            System.out.println(sql);
            con.createStatement().execute(sql);
            return "{\"status\":\"success\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"Database error occurred. " + e.getMessage() + "\"}";
        } finally {
            DBUtil.closeConnection();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String put(@PathParam("id") int id, Payload payload) {
        System.out.println("PUT invoked");
        Customer customer = payload.getCustomer();
        Connection con;
        try {
            con = DBUtil.getConnection();
            String sql = "update Customer set firstName='" + customer.getFirstName() + "', lastName='" + customer
                    .getLastName() + "', email='" + customer.getEmail() + "', address='" + customer.getAddress() + "'" +
                    ", creditLimit=" + customer.getCreditLimit() + " where id=" + id;
            System.out.println(sql);
            con.createStatement().execute(sql);
            return "{\"status\":\"success\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"Database error occurred. " + e.getMessage() + "\"}";
        } finally {
            DBUtil.closeConnection();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") int id) {
        System.out.println("DELETE invoked");
        Connection con;
        try {
            con = DBUtil.getConnection();
            String sql = "delete from Customer where id=" + id;
            System.out.println(sql);
            con.createStatement().execute(sql);
            return "{\"status\":\"success\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"database error occurred. " + e.getMessage() + "\"}";
        } finally {
            DBUtil.closeConnection();
        }
    }
}
