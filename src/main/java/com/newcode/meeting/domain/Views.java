package com.newcode.meeting.domain;

public final class Views {

    public interface Id {
    }

    public interface Image {
    }

    public interface Name extends Id {
    }

    public interface IdName extends Name {

    }

    public interface City extends IdName {
    }

    public interface IdMessage extends Id, Name {
    }

    public interface ChatIdName extends IdName {
    }

    public interface FullProfile extends IdName, IdMessage, City {
    }

    public interface FullProfileDetail extends IdName, City {

    }

    public interface FullProfileDetailImage extends FullProfileDetail, Image {
    }

    public interface FullProfileDetailAndEmail extends FullProfileDetailImage {
    }

    public interface FullField extends FullProfile {
    }
}
