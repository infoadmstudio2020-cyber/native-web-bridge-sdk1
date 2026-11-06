package com.roni.library.router.service;

import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.router.NativeRouter;
import com.roni.library.service.BaseService;

/**
 * Layer 1 ecosystem integration service exposing NativeRouter to {@link com.roni.library.service.ServiceRegistry}.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRouterService extends BaseService {

    public static final String SERVICE_ID = "service.router";
    public static final String SERVICE_NAME = "NativeRouterService";

    private final NativeRouter router;

    public NativeRouterService() {
        this(NativeRouter.getInstance());
    }

    public NativeRouterService(NativeRouter router) {
        super(SERVICE_ID, 100);
        this.router = router != null ? router : NativeRouter.getInstance();
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    protected void onInitialize() {
        if (router != null && !router.isInitialized()) {
            router.initialize();
        }
    }

    @Override
    protected void onServiceStart() {
        if (router != null && !router.isInitialized()) {
            router.initialize();
        }
    }

    @Override
    protected void onServiceStop() {
        // Router can remain active or be paused
    }

    @Override
    protected void onDispose() {
        if (router != null && !router.isDisposed()) {
            router.release();
        }
    }

    public NativeRouter getRouter() {
        return router;
    }

    public void registerRoute(String pattern, IApiHandler handler) {
        if (router != null) {
            router.registerRoute(pattern, handler);
        }
    }
}
