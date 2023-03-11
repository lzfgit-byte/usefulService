package com.ilzf.browser.impl;


import com.teamdev.jxbrowser.chromium.events.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class LoadListenerImpl implements LoadListener {
    @Override
    public void onStartLoadingFrame(StartLoadingEvent startLoadingEvent) {
        log.info("onStartLoadingFrame");
    }

    @Override
    public void onProvisionalLoadingFrame(ProvisionalLoadingEvent provisionalLoadingEvent) {
        log.info("onProvisionalLoadingFrame");
    }

    @Override
    public void onFinishLoadingFrame(FinishLoadingEvent finishLoadingEvent) {
        log.info("onFinishLoadingFrame");
    }

    @Override
    public void onFailLoadingFrame(FailLoadingEvent failLoadingEvent) {
        log.info("onFailLoadingFrame");
    }

    @Override
    public void onDocumentLoadedInMainFrame(LoadEvent loadEvent) {
        log.info("onDocumentLoadedInMainFrame");
    }
}
