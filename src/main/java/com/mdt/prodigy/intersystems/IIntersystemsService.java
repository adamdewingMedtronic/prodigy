package com.mdt.prodigy.intersystems;

public interface IIntersystemsService {

    void OnInit() throws Exception;

    Object ProcessInput(Object object) throws Exception;

    void OnTearDown() throws Exception;
}
