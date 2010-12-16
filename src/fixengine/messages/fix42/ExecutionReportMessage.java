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
package fixengine.messages.fix42;

import fixengine.messages.AbstractMessage;
import fixengine.messages.MessageHeader;
import fixengine.messages.MessageVisitor;
import fixengine.messages.Required;
import fixengine.tags.AvgPx;
import fixengine.tags.ClOrdID;
import fixengine.tags.CumQty;
import fixengine.tags.Currency;
import fixengine.tags.ExDestination;
import fixengine.tags.ExecID;
import fixengine.tags.ExecRestatementReason;
import fixengine.tags.ExecType;
import fixengine.tags.LastMkt;
import fixengine.tags.LastPx;
import fixengine.tags.LastShares;
import fixengine.tags.LeavesQty;
import fixengine.tags.MaturityMonthYear;
import fixengine.tags.OrdRejReason;
import fixengine.tags.OrdStatus;
import fixengine.tags.OrdType;
import fixengine.tags.OrderID;
import fixengine.tags.OrderQty;
import fixengine.tags.OrigClOrdID;
import fixengine.tags.Price;
import fixengine.tags.SecurityType;
import fixengine.tags.Symbol;
import fixengine.tags.Text;
import fixengine.tags.fix42.TimeInForce;
import fixengine.tags.TransactTime;
import fixengine.tags.fix42.Side;
import fixengine.tags.fix42.ExecTransType;

/**
 * @author Pekka Enberg 
 */
public class ExecutionReportMessage extends AbstractMessage implements fixengine.messages.ExecutionReportMessage {
    public ExecutionReportMessage(MessageHeader header) {
        super(header);

        fields();
    }

    @Override public void apply(MessageVisitor visitor) {
        visitor.visit(this);
    }

    protected void fields() {
        field(OrderID.Tag());
        field(ClOrdID.Tag(), Required.NO);
        field(OrigClOrdID.Tag(), Required.NO);
        field(ExecID.Tag());
        field(ExecTransType.Tag());
        field(ExecType.Tag());
        field(OrdStatus.Tag());
        field(OrdRejReason.Tag(), Required.NO);
        field(ExecRestatementReason.Tag(), Required.NO);
        field(Symbol.Tag());
        field(SecurityType.Tag(), Required.NO);
        field(MaturityMonthYear.Tag(), Required.NO);
        field(Side.Tag());
        field(OrderQty.TAG);
        field(LastShares.TAG, Required.NO);
        field(LastPx.TAG, Required.NO);
        field(LeavesQty.TAG);
        field(OrdType.Tag(), Required.NO);
        field(Price.TAG, new Required() {
            @Override public boolean isRequired() {
                return OrdType.Limit().equals(getEnum(OrdType.Tag()));
            }
        });
        field(TimeInForce.Tag(), Required.NO);
        field(CumQty.TAG);
        field(AvgPx.TAG);
        field(TransactTime.TAG, Required.NO);
        field(Text.Tag(), Required.NO);
        field(ExDestination.Tag(), Required.NO);
        field(LastMkt.Tag(), Required.NO);
        field(Currency.Tag(), Required.NO);
    }
}
