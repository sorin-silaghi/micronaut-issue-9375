package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.ArgumentUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.web.router.UriRouteMatch;
import io.micronaut.web.router.version.DefaultVersionProvider;
import io.micronaut.web.router.version.RouteVersionFilter;
import io.micronaut.web.router.version.RoutesVersioningConfiguration;
import io.micronaut.web.router.version.resolution.HeaderVersionResolverConfiguration;
import io.micronaut.web.router.version.resolution.RequestVersionResolver;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.micronaut.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;

@Replaces(RouteVersionFilter.class)
@Singleton
public class RouteVersionFilterReplacement extends RouteVersionFilter {

    private final List<RequestVersionResolver> resolvingStrategies;
    private final DefaultVersionProvider defaultVersionProvider;

    @Nullable
    RoutesVersioningConfiguration routesVersioningConfiguration;
    @Nullable
    HeaderVersionResolverConfiguration headerVersionResolverConfiguration;


    /**
     * Creates a {@link RouteVersionFilter} with a collection of {@link RequestVersionResolver}.
     *
     * @param resolvingStrategies    A list of {@link RequestVersionResolver} beans to extract version from HTTP request
     * @param defaultVersionProvider The Default Version Provider
     */
    public RouteVersionFilterReplacement(List<RequestVersionResolver> resolvingStrategies,
                                         @Nullable DefaultVersionProvider defaultVersionProvider,
                                         @Nullable RoutesVersioningConfiguration routesVersioningConfiguration,
                                         @Nullable HeaderVersionResolverConfiguration headerVersionResolverConfiguration) {
        super(resolvingStrategies, defaultVersionProvider);
        this.resolvingStrategies = resolvingStrategies;
        this.defaultVersionProvider = defaultVersionProvider;
        this.routesVersioningConfiguration = routesVersioningConfiguration;
        this.headerVersionResolverConfiguration = headerVersionResolverConfiguration;
    }


    @Override
    public <T, R> Predicate<UriRouteMatch<T, R>> filter(HttpRequest<?> request) {

        ArgumentUtils.requireNonNull("request", request);

        if (resolvingStrategies == null || resolvingStrategies.isEmpty()) {
            return match -> true;
        }

        Optional<String> defaultVersion = defaultVersionProvider == null ? Optional.empty() : Optional.of(defaultVersionProvider.resolveDefaultVersion());

        Optional<String> version = resolveVersion(request);

        return match -> {
            Optional<String> routeVersion = getVersion(match);
            if (routeVersion.isPresent()) {
                if (shouldFilterReturnTrueForPreFlightRequest(request)) {
                    return true;
                }
                return matchIfRouteIsVersioned(request, version.orElse(defaultVersion.orElse(null)), routeVersion.get());
            }
            return matchIfRouteIsNotVersioned(request, version.orElse(null));

        };
    }


    boolean shouldFilterReturnTrueForPreFlightRequest(HttpRequest<?> request) {
        if (!isPreflightRequest(request)) {
            return false;
        }
        if (routesVersioningConfiguration != null && routesVersioningConfiguration.isEnabled() && headerVersionResolverConfiguration != null) {
            return headerVersionResolverConfiguration.getNames().stream().anyMatch(expectedHeaderName -> request.getHeaders().getAll(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS).contains(expectedHeaderName));
        }
        return true;
    }
    static boolean isPreflightRequest(HttpRequest<?> request) {
        HttpHeaders headers = request.getHeaders();
        Optional<String> origin = request.getOrigin();
        return origin.isPresent() && headers.contains(ACCESS_CONTROL_REQUEST_METHOD) && HttpMethod.OPTIONS == request.getMethod();
    }
}
