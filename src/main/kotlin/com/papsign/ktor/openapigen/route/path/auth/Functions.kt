package com.papsign.ktor.openapigen.route.path.auth

import com.papsign.ktor.openapigen.modules.RouteOpenAPIModule
import com.papsign.ktor.openapigen.route.method
import com.papsign.ktor.openapigen.route.preHandle
import com.papsign.ktor.openapigen.route.response.OpenAPIPipelineAuthContext
import io.ktor.http.HttpMethod
import io.ktor.util.pipeline.ContextDsl

@ContextDsl
inline fun <reified P : Any, reified R : Any, A> OpenAPIAuthenticatedRoute<A>.get(
    vararg modules: RouteOpenAPIModule,
    example: R? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P) -> Unit
) = route(HttpMethod.Get, modules, example, body)

@ContextDsl
inline fun <reified P : Any, reified R : Any, reified B : Any, A> OpenAPIAuthenticatedRoute<A>.post(
    vararg modules: RouteOpenAPIModule,
    exampleResponse: R? = null,
    exampleRequest: B? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P, B) -> Unit
) = route(HttpMethod.Post, modules, exampleResponse, exampleRequest, body)

@ContextDsl
inline fun <reified P : Any, reified R : Any, reified B : Any, A> OpenAPIAuthenticatedRoute<A>.put(
    vararg modules: RouteOpenAPIModule,
    exampleResponse: R? = null,
    exampleRequest: B? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P, B) -> Unit
) = route(HttpMethod.Put, modules, exampleResponse, exampleRequest, body)

@ContextDsl
inline fun <reified P : Any, reified R : Any, reified B : Any, A> OpenAPIAuthenticatedRoute<A>.patch(
    vararg modules: RouteOpenAPIModule,
    exampleResponse: R? = null,
    exampleRequest: B? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P, B) -> Unit
) = route(HttpMethod.Patch, modules, exampleResponse, exampleRequest, body)

@ContextDsl
inline fun <reified P : Any, reified R : Any, A> OpenAPIAuthenticatedRoute<A>.delete(
    vararg modules: RouteOpenAPIModule,
    example: R? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P) -> Unit
) = route(HttpMethod.Delete, modules, example, body)

@ContextDsl
inline fun <reified P : Any, reified R : Any, A> OpenAPIAuthenticatedRoute<A>.head(
    vararg modules: RouteOpenAPIModule,
    example: R? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P) -> Unit
) = route(HttpMethod.Head, modules, example, body)

@ContextDsl
inline fun <reified P : Any, reified R : Any, reified B : Any, A> OpenAPIAuthenticatedRoute<A>.route(
    method: HttpMethod,
    modules: Array<out RouteOpenAPIModule>,
    exampleResponse: R? = null,
    exampleRequest: B? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P, B) -> Unit
) {
    method(method).apply { modules.forEach(provider::registerModule) }.handle(exampleResponse, exampleRequest, body)
}

@ContextDsl
inline fun <reified P : Any, reified R : Any, A> OpenAPIAuthenticatedRoute<A>.route(
    method: HttpMethod,
    modules: Array<out RouteOpenAPIModule>,
    exampleResponse: R? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P) -> Unit
) {
    method(method).apply { modules.forEach(provider::registerModule) }.handle(exampleResponse, body)
}

@ContextDsl
inline fun <reified P : Any, reified R : Any, reified B : Any, A> OpenAPIAuthenticatedRoute<A>.handle(
    exampleResponse: R? = null,
    exampleRequest: B? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P, B) -> Unit
) {
    preHandle<P, R, B, OpenAPIAuthenticatedRoute<A>>(exampleResponse, exampleRequest) {
        handle(body)
    }
}

@ContextDsl
inline fun <reified P : Any, reified R : Any,  A> OpenAPIAuthenticatedRoute<A>.handle(
    exampleResponse: R? = null,
    crossinline body: suspend OpenAPIPipelineAuthContext<A, R>.(P) -> Unit
) {
    preHandle<P, R, Unit, OpenAPIAuthenticatedRoute<A>>(exampleResponse, Unit) {
        handle(body)
    }
}

suspend fun <A> OpenAPIPipelineAuthContext<A, *>.principal() = authProvider.getAuth(pipeline)
