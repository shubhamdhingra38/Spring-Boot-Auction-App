package com.example.auctionapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Auction does not belong to user")
public class AuctionDoesNotBelongToUserException extends Exception{
}
