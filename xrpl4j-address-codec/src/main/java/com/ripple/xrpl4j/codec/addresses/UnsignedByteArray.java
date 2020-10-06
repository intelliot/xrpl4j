package com.ripple.xrpl4j.codec.addresses;

import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnsignedByteArray {

  private final List<UnsignedByte> unsignedBytes;

  public static UnsignedByteArray of(final byte[] bytes) {
    Objects.requireNonNull(bytes);

    List<UnsignedByte> unsignedBytes = new ArrayList<>(bytes.length);
    for (int i = 0; i < bytes.length; i++) {
      unsignedBytes.add(i, UnsignedByte.of(bytes[i]));
    }
    return new UnsignedByteArray(unsignedBytes);
  }

  public UnsignedByteArray(final List<UnsignedByte> unsignedBytes) {
    Objects.requireNonNull(unsignedBytes);

    Objects.requireNonNull(unsignedBytes);
    this.unsignedBytes = unsignedBytes;
  }

  public List<UnsignedByte> getUnsignedBytes() {
    return unsignedBytes;
  }

  public byte[] toByteArray() {
    byte[] bytes = new byte[unsignedBytes.size()];

    for (int i = 0; i < unsignedBytes.size(); i++) {
      bytes[i] = unsignedBytes.get(i).asByte();
    }

    return bytes;
  }

  public String hexValue() {
    return BaseEncoding.base16().encode(toByteArray());
  }

  public UnsignedByteArray concat(final UnsignedByteArray bytes) {
    Objects.requireNonNull(bytes);

    unsignedBytes.addAll(bytes.getUnsignedBytes());
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UnsignedByteArray)) {
      return false;
    }
    UnsignedByteArray that = (UnsignedByteArray) o;
    return getUnsignedBytes().equals(that.getUnsignedBytes());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUnsignedBytes());
  }
}