package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import p027b.p068d.C1775h;
import p027b.p068d.p069a.C1756i;

@JsonObject
public class CacheRecord<T> {
    @JsonField(name = {"data"})
    private T data;
    @JsonField(name = {"dataClassName"})
    private String dataClassName;
    @JsonField(name = {"dataCollectionClassName"})
    private String dataCollectionClassName;
    @JsonField(name = {"dataKeyMapClassName"})
    private String dataKeyMapClassName;
    @JsonField(name = {"expirable"})
    private Boolean expirable;
    @JsonField(name = {"lifeTime"})
    private Long lifeTime;
    @JsonField(name = {"sizeOnMb"})
    private float sizeOnMb;
    @JsonField(name = {"source"}, typeConverter = SourceEnumConverter.class)
    private C1775h source;
    @JsonField(name = {"timeAtWhichWasPersisted"})
    private long timeAtWhichWasPersisted;

    public CacheRecord(C1756i<T> c1756i) {
        this.source = c1756i.m5393a();
        this.data = c1756i.m5396b();
        this.timeAtWhichWasPersisted = c1756i.m5397c();
        this.dataClassName = c1756i.m5400f();
        this.dataCollectionClassName = c1756i.m5401g();
        this.dataKeyMapClassName = c1756i.m5402h();
        this.expirable = c1756i.m5403i();
        this.lifeTime = c1756i.m5398d();
        this.sizeOnMb = c1756i.m5399e();
    }

    public C1756i<T> ToRxCacheRecord() {
        C1756i<T> c1756i = new C1756i(getData(), getExpirable(), getLifeTime());
        c1756i.m5395a(getSource());
        c1756i.m5394a(getSizeOnMb());
        return c1756i;
    }

    public T getData() {
        return this.data;
    }

    public String getDataClassName() {
        return this.dataClassName;
    }

    public String getDataCollectionClassName() {
        return this.dataCollectionClassName;
    }

    public String getDataKeyMapClassName() {
        return this.dataKeyMapClassName;
    }

    public Boolean getExpirable() {
        return this.expirable;
    }

    public Long getLifeTime() {
        return this.lifeTime;
    }

    public float getSizeOnMb() {
        return this.sizeOnMb;
    }

    public C1775h getSource() {
        return this.source;
    }

    public long getTimeAtWhichWasPersisted() {
        return this.timeAtWhichWasPersisted;
    }

    public void setData(T t) {
        this.data = t;
    }

    public void setDataClassName(String str) {
        this.dataClassName = str;
    }

    public void setDataCollectionClassName(String str) {
        this.dataCollectionClassName = str;
    }

    public void setDataKeyMapClassName(String str) {
        this.dataKeyMapClassName = str;
    }

    public void setExpirable(Boolean bool) {
        this.expirable = bool;
    }

    public void setLifeTime(Long l) {
        this.lifeTime = l;
    }

    public void setSizeOnMb(float f) {
        this.sizeOnMb = f;
    }

    public void setSource(C1775h c1775h) {
        this.source = c1775h;
    }

    public void setTimeAtWhichWasPersisted(long j) {
        this.timeAtWhichWasPersisted = j;
    }
}
