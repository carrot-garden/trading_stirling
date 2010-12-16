/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fixengine.messages;

import java.nio.ByteBuffer;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import fixengine.tags.BeginString;
import fixengine.tags.BodyLength;
import fixengine.tags.DeliverToCompID;
import fixengine.tags.MsgSeqNum;
import fixengine.tags.MsgType;
import fixengine.tags.OnBehalfOfCompID;
import fixengine.tags.OrigSendingTime;
import fixengine.tags.PossDupFlag;
import fixengine.tags.PossResend;
import fixengine.tags.SenderCompID;
import fixengine.tags.SenderSubID;
import fixengine.tags.SendingTime;
import fixengine.tags.TargetCompID;
import fixengine.tags.TargetSubID;
import fixengine.tags.SenderLocationID;

/**
 * @author Pekka Enberg 
 */
public class MessageHeader extends FieldContainer implements Parseable {
    private static final Minutes MAX_TIME_DIFFERENCE = Minutes.TWO;

    private final FieldContainer head = new FieldContainer();

    public MessageHeader(String msgType) {
        this();

        setMsgType(msgType);
    }

    public MessageHeader() {
        head.field(BeginString.Tag());
        head.field(BodyLength.TAG);
        head.field(MsgType.Tag());
        field(SenderCompID.Tag());
        field(SenderSubID.Tag(), Required.NO);
        field(TargetCompID.Tag());
        field(TargetSubID.Tag(), Required.NO);
        field(OnBehalfOfCompID.Tag(), Required.NO);
        field(DeliverToCompID.Tag(), Required.NO);
        field(MsgSeqNum.TAG);
        field(PossDupFlag.TAG, Required.NO);
        field(PossResend.TAG, Required.NO);
        field(SendingTime.TAG);
        field(OrigSendingTime.TAG, new Required() {
            @Override public boolean isRequired() {
                return hasValue(PossDupFlag.TAG) && getBoolean(PossDupFlag.TAG);
            }
        });
        field(SenderLocationID.Tag(), Required.NO);
    }

    @Override public void parse(ByteBuffer b) {
        parseHeadField(b, BeginString.Tag());
        parseHeadField(b, BodyLength.TAG);
        trailer(b);
        parseHeadField(b, MsgType.Tag());
        super.parse(b);
    }

    private void trailer(ByteBuffer b) {
        int checkSumPosition = b.position() + getBodyLength();
        b.limit(checkSumPosition);
    }

    private void parseHeadField(ByteBuffer b, Tag<?> tag) {
        Tag.parseTag(b);
        Field field = head.lookup(tag.value());
        field.parse(b);
    }

    public String getBeginString() {
        return head.getString(BeginString.Tag());
    }

    public void setBeginString(String beginString) {
        head.setString(BeginString.Tag(), beginString);
    }

    public int getBodyLength() {
        return head.getInteger(BodyLength.TAG);
    }

    public String getMsgType() {
        return head.getString(MsgType.Tag());
    }

    public void setMsgType(String msgType) {
        head.setString(MsgType.Tag(), msgType);
    }

    public boolean isPointToPoint() {
        return !hasValue(OnBehalfOfCompID.Tag()) && !hasValue(DeliverToCompID.Tag());
    }

    public boolean hasAccurateSendingTime(DateTime currentTime) {
        if (!hasValue(SendingTime.TAG)) {
            return true;
        }
        Minutes difference = Minutes.minutesBetween(currentTime, getDateTime(SendingTime.TAG));
        return Math.abs(difference.getMinutes()) < MAX_TIME_DIFFERENCE.getMinutes();
    }

    public boolean hasOrigSendTimeAfterSendingTime() {
        if (!getBoolean(PossDupFlag.TAG) || !hasValue(OrigSendingTime.TAG)) {
            return true;
        }
        return !getDateTime(OrigSendingTime.TAG).isAfter(getDateTime(SendingTime.TAG));
    }

    public Message newMessage(MessageFactory messageFactory) {
        return messageFactory.create(getMsgType(), this);
    }
}
