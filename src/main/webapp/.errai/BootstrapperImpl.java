package org.jboss.errai.ioc.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.errai.common.client.api.extension.InitVotes;
import org.jboss.errai.enterprise.client.cdi.CDIEventTypeLookup;
import org.jboss.errai.enterprise.client.cdi.api.CDI;
import org.jboss.errai.enterprise.client.jaxrs.JaxrsModuleBootstrapper;
import org.jboss.errai.ioc.client.api.builtin.IOCBeanManagerProvider;
import org.jboss.errai.ioc.client.container.CreationalCallback;
import org.jboss.errai.ioc.client.container.CreationalContext;
import org.jboss.errai.ioc.client.container.IOCBeanManager;
import org.jboss.errai.ioc.client.container.ProxyResolver;
import org.jboss.pressgang.ccms.ui.client.local.App;
import org.jboss.pressgang.ccms.ui.client.local.AppController;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.WelcomePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.EditableView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.PresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoriesFilteredResultsAndDetailsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryFilteredResultsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImageFilteredResultsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagePresenterBase;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagesFilteredResultsAndDetailsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.*;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.*;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicPresenter.Display;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.WelcomeView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.category.CategoriesFilteredResultsAndCategoryView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.category.CategoryFilteredResultsView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.category.CategoryView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.image.ImageFilteredResultsView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.image.ImageView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.image.ImagesFilteredResultsAndImageView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.tag.*;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.*;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topicsearch.SearchResultsAndTopicView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topicsearch.SearchResultsView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topicsearch.SearchView;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.inject.Provider;
import java.lang.annotation.Annotation;

public class BootstrapperImpl implements Bootstrapper {
    {
        new CDI().initLookupTable(CDIEventTypeLookup.get());
        new JaxrsModuleBootstrapper().run();
    }

    private final Default _1998831462Default_33424704 = new Default() {
        public Class annotationType() {
            return Default.class;
        }
    };
    private final Any _1998831462Any_703259 = new Any() {
        public Class annotationType() {
            return Any.class;
        }
    };
    private final Annotation[] arrayOf_19635043Annotation_27515521 = new Annotation[]{_1998831462Default_33424704, _1998831462Any_703259};
    private final BootstrapperInjectionContext injContext = new BootstrapperInjectionContext();
    private final CreationalContext context = injContext.getRootContext();
    private final CreationalCallback<IOCBeanManagerProvider> inj4219_IOCBeanManagerProvider_creational = new CreationalCallback<IOCBeanManagerProvider>() {
        public IOCBeanManagerProvider getInstance(final CreationalContext context) {
            final IOCBeanManagerProvider inj4206_IOCBeanManagerProvider = new IOCBeanManagerProvider();
            context.addBean(context.getBeanReference(IOCBeanManagerProvider.class, arrayOf_19635043Annotation_27515521), inj4206_IOCBeanManagerProvider);
            return inj4206_IOCBeanManagerProvider;
        }
    };
    private final IOCBeanManagerProvider inj4206_IOCBeanManagerProvider = inj4219_IOCBeanManagerProvider_creational.getInstance(context);
    private final CreationalCallback<App> inj4221_App_creational = new CreationalCallback<App>() {
        public App getInstance(final CreationalContext context) {
            final App inj4220_App = new App();
            context.addBean(context.getBeanReference(App.class, arrayOf_19635043Annotation_27515521), inj4220_App);
            final AppController_inj4222_proxy inj4222_proxy = new AppController_inj4222_proxy();
            context.addUnresolvedProxy(new ProxyResolver<AppController>() {
                public void resolve(AppController obj) {
                    inj4222_proxy.__$setProxiedInstance$(obj);
                    context.addProxyReference(inj4222_proxy, obj);
                }
            }, AppController.class, arrayOf_19635043Annotation_27515521);
            _$477517530_appController(inj4220_App, inj4222_proxy);
            InitVotes.registerOneTimeInitCallback(new Runnable() {
                public void run() {
                    inj4220_App.startApp();
                }
            });
            return inj4220_App;
        }
    };
    private final App inj4220_App = inj4221_App_creational.getInstance(context);
    private final CreationalCallback<HandlerManager> inj4198_HandlerManager_creational = new CreationalCallback<HandlerManager>() {
        public HandlerManager getInstance(CreationalContext pContext) {
            HandlerManager var2 = _$477517530_produceEventBus(inj4220_App);
            context.addBean(context.getBeanReference(HandlerManager.class, arrayOf_19635043Annotation_27515521), var2);
            return var2;
        }
    };
    private final CreationalCallback<AppController> inj4224_AppController_creational = new CreationalCallback<AppController>() {
        public AppController getInstance(final CreationalContext context) {
            final AppController inj4223_AppController = new AppController();
            context.addBean(context.getBeanReference(AppController.class, arrayOf_19635043Annotation_27515521), inj4223_AppController);
            _2098016610_manager(inj4223_AppController, inj4206_IOCBeanManagerProvider.get());
            _2098016610_eventBus(inj4223_AppController, _$477517530_produceEventBus(inj4220_App));
            return inj4223_AppController;
        }
    };
    private final AppController inj4223_AppController = inj4224_AppController_creational.getInstance(context);
    private final CreationalCallback<TopicView> inj4227_TopicView_creational = new CreationalCallback<TopicView>() {
        public TopicView getInstance(final CreationalContext context) {
            final TopicView inj2644_TopicView = new TopicView();
            context.addBean(context.getBeanReference(TopicView.class, arrayOf_19635043Annotation_27515521), inj2644_TopicView);
            return inj2644_TopicView;
        }
    };
    private final CreationalCallback<TopicPresenter> inj4226_TopicPresenter_creational = new CreationalCallback<TopicPresenter>() {
        public TopicPresenter getInstance(final CreationalContext context) {
            final TopicPresenter inj4225_TopicPresenter = new TopicPresenter();
            context.addBean(context.getBeanReference(TopicPresenter.class, arrayOf_19635043Annotation_27515521), inj4225_TopicPresenter);
            _392006560_display(inj4225_TopicPresenter, inj4227_TopicView_creational.getInstance(context));
            _1500219897_eventBus(inj4225_TopicPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4225_TopicPresenter;
        }
    };
    private final CreationalCallback<SearchResultsView> inj4230_SearchResultsView_creational = new CreationalCallback<SearchResultsView>() {
        public SearchResultsView getInstance(final CreationalContext context) {
            final SearchResultsView inj2652_SearchResultsView = new SearchResultsView();
            context.addBean(context.getBeanReference(SearchResultsView.class, arrayOf_19635043Annotation_27515521), inj2652_SearchResultsView);
            return inj2652_SearchResultsView;
        }
    };
    private final CreationalCallback<SearchResultsPresenter> inj4229_SearchResultsPresenter_creational = new CreationalCallback<SearchResultsPresenter>() {
        public SearchResultsPresenter getInstance(final CreationalContext context) {
            final SearchResultsPresenter inj4228_SearchResultsPresenter = new SearchResultsPresenter();
            context.addBean(context.getBeanReference(SearchResultsPresenter.class, arrayOf_19635043Annotation_27515521), inj4228_SearchResultsPresenter);
            _954332697_display(inj4228_SearchResultsPresenter, inj4230_SearchResultsView_creational.getInstance(context));
            _954332697_topicViewDisplay(inj4228_SearchResultsPresenter, inj4227_TopicView_creational.getInstance(context));
            _1500219897_eventBus(inj4228_SearchResultsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4228_SearchResultsPresenter;
        }
    };
    private final CreationalCallback<CategoryView> inj4233_CategoryView_creational = new CreationalCallback<CategoryView>() {
        public CategoryView getInstance(final CreationalContext context) {
            final CategoryView inj2677_CategoryView = new CategoryView();
            context.addBean(context.getBeanReference(CategoryView.class, arrayOf_19635043Annotation_27515521), inj2677_CategoryView);
            return inj2677_CategoryView;
        }
    };
    private final CreationalCallback<CategoryPresenter> inj4232_CategoryPresenter_creational = new CreationalCallback<CategoryPresenter>() {
        public CategoryPresenter getInstance(final CreationalContext context) {
            final CategoryPresenter inj4231_CategoryPresenter = new CategoryPresenter();
            context.addBean(context.getBeanReference(CategoryPresenter.class, arrayOf_19635043Annotation_27515521), inj4231_CategoryPresenter);
            _1814806242_display(inj4231_CategoryPresenter, inj4233_CategoryView_creational.getInstance(context));
            _1500219897_eventBus(inj4231_CategoryPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4231_CategoryPresenter;
        }
    };
    private final CreationalCallback<TagsFilteredResultsAndTagView> inj4236_TagsFilteredResultsAndTagView_creational = new CreationalCallback<TagsFilteredResultsAndTagView>() {
        public TagsFilteredResultsAndTagView getInstance(final CreationalContext context) {
            final TagsFilteredResultsAndTagView inj4034_TagsFilteredResultsAndTagView = new TagsFilteredResultsAndTagView();
            context.addBean(context.getBeanReference(TagsFilteredResultsAndTagView.class, arrayOf_19635043Annotation_27515521), inj4034_TagsFilteredResultsAndTagView);
            return inj4034_TagsFilteredResultsAndTagView;
        }
    };
    private final CreationalCallback<TagFilteredResultsView> inj4237_TagFilteredResultsView_creational = new CreationalCallback<TagFilteredResultsView>() {
        public TagFilteredResultsView getInstance(final CreationalContext context) {
            final TagFilteredResultsView inj4033_TagFilteredResultsView = new TagFilteredResultsView();
            context.addBean(context.getBeanReference(TagFilteredResultsView.class, arrayOf_19635043Annotation_27515521), inj4033_TagFilteredResultsView);
            return inj4033_TagFilteredResultsView;
        }
    };
    private final CreationalCallback<TagView> inj4238_TagView_creational = new CreationalCallback<TagView>() {
        public TagView getInstance(final CreationalContext context) {
            final TagView inj4036_TagView = new TagView();
            context.addBean(context.getBeanReference(TagView.class, arrayOf_19635043Annotation_27515521), inj4036_TagView);
            return inj4036_TagView;
        }
    };
    private final CreationalCallback<TagProjectsView> inj4239_TagProjectsView_creational = new CreationalCallback<TagProjectsView>() {
        public TagProjectsView getInstance(final CreationalContext context) {
            final TagProjectsView inj4035_TagProjectsView = new TagProjectsView();
            context.addBean(context.getBeanReference(TagProjectsView.class, arrayOf_19635043Annotation_27515521), inj4035_TagProjectsView);
            return inj4035_TagProjectsView;
        }
    };
    private final CreationalCallback<TagCategoriesView> inj4240_TagCategoriesView_creational = new CreationalCallback<TagCategoriesView>() {
        public TagCategoriesView getInstance(final CreationalContext context) {
            final TagCategoriesView inj4037_TagCategoriesView = new TagCategoriesView();
            context.addBean(context.getBeanReference(TagCategoriesView.class, arrayOf_19635043Annotation_27515521), inj4037_TagCategoriesView);
            return inj4037_TagCategoriesView;
        }
    };
    private final CreationalCallback<TagsFilteredResultsAndDetailsPresenter> inj4235_TagsFilteredResultsAndTagPresenter_creational = new CreationalCallback<TagsFilteredResultsAndDetailsPresenter>() {
        public TagsFilteredResultsAndDetailsPresenter getInstance(final CreationalContext context) {
            final TagsFilteredResultsAndDetailsPresenter inj4234_TagsFilteredResultsAndTagPresenter = new TagsFilteredResultsAndDetailsPresenter();
            context.addBean(context.getBeanReference(TagsFilteredResultsAndDetailsPresenter.class, arrayOf_19635043Annotation_27515521), inj4234_TagsFilteredResultsAndTagPresenter);
            _$525173285_display(inj4234_TagsFilteredResultsAndTagPresenter, inj4236_TagsFilteredResultsAndTagView_creational.getInstance(context));
            _$525173285_filteredResultsDisplay(inj4234_TagsFilteredResultsAndTagPresenter, inj4237_TagFilteredResultsView_creational.getInstance(context));
            _$525173285_resultDisplay(inj4234_TagsFilteredResultsAndTagPresenter, inj4238_TagView_creational.getInstance(context));
            _$525173285_projectsDisplay(inj4234_TagsFilteredResultsAndTagPresenter, inj4239_TagProjectsView_creational.getInstance(context));
            _$525173285_categoriesDisplay(inj4234_TagsFilteredResultsAndTagPresenter, inj4240_TagCategoriesView_creational.getInstance(context));
            _1500219897_eventBus(inj4234_TagsFilteredResultsAndTagPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4234_TagsFilteredResultsAndTagPresenter;
        }
    };
    private final CreationalCallback<TagFilteredResultsPresenter> inj4242_TagFilteredResultsPresenter_creational = new CreationalCallback<TagFilteredResultsPresenter>() {
        public TagFilteredResultsPresenter getInstance(final CreationalContext context) {
            final TagFilteredResultsPresenter inj4241_TagFilteredResultsPresenter = new TagFilteredResultsPresenter();
            context.addBean(context.getBeanReference(TagFilteredResultsPresenter.class, arrayOf_19635043Annotation_27515521), inj4241_TagFilteredResultsPresenter);
            _1748359463_display(inj4241_TagFilteredResultsPresenter, inj4237_TagFilteredResultsView_creational.getInstance(context));
            _1500219897_eventBus(inj4241_TagFilteredResultsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4241_TagFilteredResultsPresenter;
        }
    };
    private final CreationalCallback<TopicTagsView> inj4245_TopicTagsView_creational = new CreationalCallback<TopicTagsView>() {
        public TopicTagsView getInstance(final CreationalContext context) {
            final TopicTagsView inj2643_TopicTagsView = new TopicTagsView();
            context.addBean(context.getBeanReference(TopicTagsView.class, arrayOf_19635043Annotation_27515521), inj2643_TopicTagsView);
            return inj2643_TopicTagsView;
        }
    };
    private final CreationalCallback<TopicTagsPresenter> inj4244_TopicTagsPresenter_creational = new CreationalCallback<TopicTagsPresenter>() {
        public TopicTagsPresenter getInstance(final CreationalContext context) {
            final TopicTagsPresenter inj4243_TopicTagsPresenter = new TopicTagsPresenter();
            context.addBean(context.getBeanReference(TopicTagsPresenter.class, arrayOf_19635043Annotation_27515521), inj4243_TopicTagsPresenter);
            _$1522350201_display(inj4243_TopicTagsPresenter, inj4245_TopicTagsView_creational.getInstance(context));
            _1500219897_eventBus(inj4243_TopicTagsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4243_TopicTagsPresenter;
        }
    };
    private final CreationalCallback<WelcomeView> inj4248_WelcomeView_creational = new CreationalCallback<WelcomeView>() {
        public WelcomeView getInstance(final CreationalContext context) {
            final WelcomeView inj2860_WelcomeView = new WelcomeView();
            context.addBean(context.getBeanReference(WelcomeView.class, arrayOf_19635043Annotation_27515521), inj2860_WelcomeView);
            return inj2860_WelcomeView;
        }
    };
    private final CreationalCallback<WelcomePresenter> inj4247_WelcomePresenter_creational = new CreationalCallback<WelcomePresenter>() {
        public WelcomePresenter getInstance(final CreationalContext context) {
            final WelcomePresenter inj4246_WelcomePresenter = new WelcomePresenter();
            context.addBean(context.getBeanReference(WelcomePresenter.class, arrayOf_19635043Annotation_27515521), inj4246_WelcomePresenter);
            _1478143022_display(inj4246_WelcomePresenter, inj4248_WelcomeView_creational.getInstance(context));
            _1500219897_eventBus(inj4246_WelcomePresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4246_WelcomePresenter;
        }
    };
    private final CreationalCallback<TagPresenter> inj4250_TagPresenter_creational = new CreationalCallback<TagPresenter>() {
        public TagPresenter getInstance(final CreationalContext context) {
            final TagPresenter inj4249_TagPresenter = new TagPresenter();
            context.addBean(context.getBeanReference(TagPresenter.class, arrayOf_19635043Annotation_27515521), inj4249_TagPresenter);
            _$774116150_display(inj4249_TagPresenter, inj4238_TagView_creational.getInstance(context));
            _1500219897_eventBus(inj4249_TagPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4249_TagPresenter;
        }
    };
    private final CreationalCallback<SearchResultsAndTopicView> inj4253_SearchResultsAndTopicView_creational = new CreationalCallback<SearchResultsAndTopicView>() {
        public SearchResultsAndTopicView getInstance(final CreationalContext context) {
            final SearchResultsAndTopicView inj2651_SearchResultsAndTopicView = new SearchResultsAndTopicView();
            context.addBean(context.getBeanReference(SearchResultsAndTopicView.class, arrayOf_19635043Annotation_27515521), inj2651_SearchResultsAndTopicView);
            return inj2651_SearchResultsAndTopicView;
        }
    };
    private final CreationalCallback<TopicXMLView> inj4254_TopicXMLView_creational = new CreationalCallback<TopicXMLView>() {
        public TopicXMLView getInstance(final CreationalContext context) {
            final TopicXMLView inj2647_TopicXMLView = new TopicXMLView();
            context.addBean(context.getBeanReference(TopicXMLView.class, arrayOf_19635043Annotation_27515521), inj2647_TopicXMLView);
            return inj2647_TopicXMLView;
        }
    };
    private final CreationalCallback<TopicRenderedView> inj4255_TopicRenderedView_creational = new CreationalCallback<TopicRenderedView>() {
        public TopicRenderedView getInstance(final CreationalContext context) {
            final TopicRenderedView inj2645_TopicRenderedView = new TopicRenderedView();
            context.addBean(context.getBeanReference(TopicRenderedView.class, arrayOf_19635043Annotation_27515521), inj2645_TopicRenderedView);
            return inj2645_TopicRenderedView;
        }
    };
    private final CreationalCallback<TopicXMLErrorsView> inj4256_TopicXMLErrorsView_creational = new CreationalCallback<TopicXMLErrorsView>() {
        public TopicXMLErrorsView getInstance(final CreationalContext context) {
            final TopicXMLErrorsView inj2648_TopicXMLErrorsView = new TopicXMLErrorsView();
            context.addBean(context.getBeanReference(TopicXMLErrorsView.class, arrayOf_19635043Annotation_27515521), inj2648_TopicXMLErrorsView);
            return inj2648_TopicXMLErrorsView;
        }
    };
    private final CreationalCallback<TopicBugsView> inj4257_TopicBugsView_creational = new CreationalCallback<TopicBugsView>() {
        public TopicBugsView getInstance(final CreationalContext context) {
            final TopicBugsView inj2646_TopicBugsView = new TopicBugsView();
            context.addBean(context.getBeanReference(TopicBugsView.class, arrayOf_19635043Annotation_27515521), inj2646_TopicBugsView);
            return inj2646_TopicBugsView;
        }
    };
    private final CreationalCallback<TopicRevisionsView> inj4258_TopicRevisionsView_creational = new CreationalCallback<TopicRevisionsView>() {
        public TopicRevisionsView getInstance(final CreationalContext context) {
            final TopicRevisionsView inj2642_TopicRevisionsView = new TopicRevisionsView();
            context.addBean(context.getBeanReference(TopicRevisionsView.class, arrayOf_19635043Annotation_27515521), inj2642_TopicRevisionsView);
            return inj2642_TopicRevisionsView;
        }
    };
    private final CreationalCallback<SearchResultsAndTopicPresenter> inj4252_SearchResultsAndTopicPresenter_creational = new CreationalCallback<SearchResultsAndTopicPresenter>() {
        public SearchResultsAndTopicPresenter getInstance(final CreationalContext context) {
            final SearchResultsAndTopicPresenter inj4251_SearchResultsAndTopicPresenter = new SearchResultsAndTopicPresenter();
            context.addBean(context.getBeanReference(SearchResultsAndTopicPresenter.class, arrayOf_19635043Annotation_27515521), inj4251_SearchResultsAndTopicPresenter);
            _1567125089_display(inj4251_SearchResultsAndTopicPresenter, inj4253_SearchResultsAndTopicView_creational.getInstance(context));
            _1567125089_topicViewDisplay(inj4251_SearchResultsAndTopicPresenter, inj4227_TopicView_creational.getInstance(context));
            _1567125089_topicXMLDisplay(inj4251_SearchResultsAndTopicPresenter, inj4254_TopicXMLView_creational.getInstance(context));
            _1567125089_topicRenderedDisplay(inj4251_SearchResultsAndTopicPresenter, inj4255_TopicRenderedView_creational.getInstance(context));
            _1567125089_topicSplitPanelRenderedDisplay(inj4251_SearchResultsAndTopicPresenter, inj4255_TopicRenderedView_creational.getInstance(context));
            _1567125089_searchResultsDisplay(inj4251_SearchResultsAndTopicPresenter, inj4230_SearchResultsView_creational.getInstance(context));
            _1567125089_topicXMLErrorsDisplay(inj4251_SearchResultsAndTopicPresenter, inj4256_TopicXMLErrorsView_creational.getInstance(context));
            _1567125089_topicTagsDisplay(inj4251_SearchResultsAndTopicPresenter, inj4245_TopicTagsView_creational.getInstance(context));
            _1567125089_topicBugsDisplay(inj4251_SearchResultsAndTopicPresenter, inj4257_TopicBugsView_creational.getInstance(context));
            _1567125089_topicRevisionsDisplay(inj4251_SearchResultsAndTopicPresenter, inj4258_TopicRevisionsView_creational.getInstance(context));
            _1500219897_eventBus(inj4251_SearchResultsAndTopicPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4251_SearchResultsAndTopicPresenter;
        }
    };
    private final CreationalCallback<SearchView> inj4261_SearchView_creational = new CreationalCallback<SearchView>() {
        public SearchView getInstance(final CreationalContext context) {
            final SearchView inj2653_SearchView = new SearchView();
            context.addBean(context.getBeanReference(SearchView.class, arrayOf_19635043Annotation_27515521), inj2653_SearchView);
            return inj2653_SearchView;
        }
    };
    private final CreationalCallback<SearchPresenter> inj4260_SearchPresenter_creational = new CreationalCallback<SearchPresenter>() {
        public SearchPresenter getInstance(final CreationalContext context) {
            final SearchPresenter inj4259_SearchPresenter = new SearchPresenter();
            context.addBean(context.getBeanReference(SearchPresenter.class, arrayOf_19635043Annotation_27515521), inj4259_SearchPresenter);
            _$192804415_eventBus(inj4259_SearchPresenter, _$477517530_produceEventBus(inj4220_App));
            _$192804415_display(inj4259_SearchPresenter, inj4261_SearchView_creational.getInstance(context));
            _1500219897_eventBus(inj4259_SearchPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4259_SearchPresenter;
        }
    };
    private final CreationalCallback<CategoryFilteredResultsView> inj4264_CategoryFilteredResultsView_creational = new CreationalCallback<CategoryFilteredResultsView>() {
        public CategoryFilteredResultsView getInstance(final CreationalContext context) {
            final CategoryFilteredResultsView inj2675_CategoryFilteredResultsView = new CategoryFilteredResultsView();
            context.addBean(context.getBeanReference(CategoryFilteredResultsView.class, arrayOf_19635043Annotation_27515521), inj2675_CategoryFilteredResultsView);
            return inj2675_CategoryFilteredResultsView;
        }
    };
    private final CreationalCallback<CategoryFilteredResultsPresenter> inj4263_CategoryFilteredResultsPresenter_creational = new CreationalCallback<CategoryFilteredResultsPresenter>() {
        public CategoryFilteredResultsPresenter getInstance(final CreationalContext context) {
            final CategoryFilteredResultsPresenter inj4262_CategoryFilteredResultsPresenter = new CategoryFilteredResultsPresenter();
            context.addBean(context.getBeanReference(CategoryFilteredResultsPresenter.class, arrayOf_19635043Annotation_27515521), inj4262_CategoryFilteredResultsPresenter);
            _$743140337_display(inj4262_CategoryFilteredResultsPresenter, inj4264_CategoryFilteredResultsView_creational.getInstance(context));
            _1500219897_eventBus(inj4262_CategoryFilteredResultsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4262_CategoryFilteredResultsPresenter;
        }
    };
    private final CreationalCallback<CategoriesFilteredResultsAndCategoryView> inj4267_CategoriesFilteredResultsAndCategoryView_creational = new CreationalCallback<CategoriesFilteredResultsAndCategoryView>() {
        public CategoriesFilteredResultsAndCategoryView getInstance(final CreationalContext context) {
            final CategoriesFilteredResultsAndCategoryView inj2676_CategoriesFilteredResultsAndCategoryView = new CategoriesFilteredResultsAndCategoryView();
            context.addBean(context.getBeanReference(CategoriesFilteredResultsAndCategoryView.class, arrayOf_19635043Annotation_27515521), inj2676_CategoriesFilteredResultsAndCategoryView);
            return inj2676_CategoriesFilteredResultsAndCategoryView;
        }
    };
    private final CreationalCallback<CategoriesFilteredResultsAndDetailsPresenter> inj4266_CategoriesFilteredResultsAndCategoryPresenter_creational = new CreationalCallback<CategoriesFilteredResultsAndDetailsPresenter>() {
        public CategoriesFilteredResultsAndDetailsPresenter getInstance(final CreationalContext context) {
            final CategoriesFilteredResultsAndDetailsPresenter inj4265_CategoriesFilteredResultsAndCategoryPresenter = new CategoriesFilteredResultsAndDetailsPresenter();
            context.addBean(context.getBeanReference(CategoriesFilteredResultsAndDetailsPresenter.class, arrayOf_19635043Annotation_27515521), inj4265_CategoriesFilteredResultsAndCategoryPresenter);
            _1643984526_display(inj4265_CategoriesFilteredResultsAndCategoryPresenter, inj4267_CategoriesFilteredResultsAndCategoryView_creational.getInstance(context));
            _1643984526_filteredResultsDisplay(inj4265_CategoriesFilteredResultsAndCategoryPresenter, inj4264_CategoryFilteredResultsView_creational.getInstance(context));
            _1643984526_resultDisplay(inj4265_CategoriesFilteredResultsAndCategoryPresenter, inj4233_CategoryView_creational.getInstance(context));
            _1500219897_eventBus(inj4265_CategoriesFilteredResultsAndCategoryPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4265_CategoriesFilteredResultsAndCategoryPresenter;
        }
    };
    private final CreationalCallback<ImagesFilteredResultsAndImageView> inj4270_ImagesFilteredResultsAndImageView_creational = new CreationalCallback<ImagesFilteredResultsAndImageView>() {
        public ImagesFilteredResultsAndImageView getInstance(final CreationalContext context) {
            final ImagesFilteredResultsAndImageView inj2393_ImagesFilteredResultsAndImageView = new ImagesFilteredResultsAndImageView();
            context.addBean(context.getBeanReference(ImagesFilteredResultsAndImageView.class, arrayOf_19635043Annotation_27515521), inj2393_ImagesFilteredResultsAndImageView);
            return inj2393_ImagesFilteredResultsAndImageView;
        }
    };
    private final CreationalCallback<ImageFilteredResultsView> inj4271_ImageFilteredResultsView_creational = new CreationalCallback<ImageFilteredResultsView>() {
        public ImageFilteredResultsView getInstance(final CreationalContext context) {
            final ImageFilteredResultsView inj2395_ImageFilteredResultsView = new ImageFilteredResultsView();
            context.addBean(context.getBeanReference(ImageFilteredResultsView.class, arrayOf_19635043Annotation_27515521), inj2395_ImageFilteredResultsView);
            return inj2395_ImageFilteredResultsView;
        }
    };
    private final CreationalCallback<ImageView> inj4272_ImageView_creational = new CreationalCallback<ImageView>() {
        public ImageView getInstance(final CreationalContext context) {
            final ImageView inj2394_ImageView = new ImageView();
            context.addBean(context.getBeanReference(ImageView.class, arrayOf_19635043Annotation_27515521), inj2394_ImageView);
            return inj2394_ImageView;
        }
    };
    private final CreationalCallback<ImagesFilteredResultsAndDetailsPresenter> inj4269_ImagesFilteredResultsAndImagePresenter_creational = new CreationalCallback<ImagesFilteredResultsAndDetailsPresenter>() {
        public ImagesFilteredResultsAndDetailsPresenter getInstance(final CreationalContext context) {
            final ImagesFilteredResultsAndDetailsPresenter inj4268_ImagesFilteredResultsAndImagePresenter = new ImagesFilteredResultsAndDetailsPresenter();
            context.addBean(context.getBeanReference(ImagesFilteredResultsAndDetailsPresenter.class, arrayOf_19635043Annotation_27515521), inj4268_ImagesFilteredResultsAndImagePresenter);
            _491824760_display(inj4268_ImagesFilteredResultsAndImagePresenter, inj4270_ImagesFilteredResultsAndImageView_creational.getInstance(context));
            _491824760_imageFilteredResultsDisplay(inj4268_ImagesFilteredResultsAndImagePresenter, inj4271_ImageFilteredResultsView_creational.getInstance(context));
            _491824760_imageDisplay(inj4268_ImagesFilteredResultsAndImagePresenter, inj4272_ImageView_creational.getInstance(context));
            _1500219897_eventBus(inj4268_ImagesFilteredResultsAndImagePresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4268_ImagesFilteredResultsAndImagePresenter;
        }
    };
    private final CreationalCallback<TagCategoriesPresenter> inj4274_TagCategoriesPresenter_creational = new CreationalCallback<TagCategoriesPresenter>() {
        public TagCategoriesPresenter getInstance(final CreationalContext context) {
            final TagCategoriesPresenter inj4273_TagCategoriesPresenter = new TagCategoriesPresenter();
            context.addBean(context.getBeanReference(TagCategoriesPresenter.class, arrayOf_19635043Annotation_27515521), inj4273_TagCategoriesPresenter);
            _$147774578_display(inj4273_TagCategoriesPresenter, inj4240_TagCategoriesView_creational.getInstance(context));
            _1500219897_eventBus(inj4273_TagCategoriesPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4273_TagCategoriesPresenter;
        }
    };
    private final CreationalCallback<TopicBugsPresenter> inj4276_TopicBugsPresenter_creational = new CreationalCallback<TopicBugsPresenter>() {
        public TopicBugsPresenter getInstance(final CreationalContext context) {
            final TopicBugsPresenter inj4275_TopicBugsPresenter = new TopicBugsPresenter();
            context.addBean(context.getBeanReference(TopicBugsPresenter.class, arrayOf_19635043Annotation_27515521), inj4275_TopicBugsPresenter);
            _1500219897_eventBus(inj4275_TopicBugsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4275_TopicBugsPresenter;
        }
    };
    private final CreationalCallback<TagProjectsPresenter> inj4278_TagProjectsPresenter_creational = new CreationalCallback<TagProjectsPresenter>() {
        public TagProjectsPresenter getInstance(final CreationalContext context) {
            final TagProjectsPresenter inj4277_TagProjectsPresenter = new TagProjectsPresenter();
            context.addBean(context.getBeanReference(TagProjectsPresenter.class, arrayOf_19635043Annotation_27515521), inj4277_TagProjectsPresenter);
            _$1369662608_display(inj4277_TagProjectsPresenter, inj4239_TagProjectsView_creational.getInstance(context));
            _1500219897_eventBus(inj4277_TagProjectsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4277_TagProjectsPresenter;
        }
    };
    private final CreationalCallback<TopicXMLErrorsPresenter> inj4280_TopicXMLErrorsPresenter_creational = new CreationalCallback<TopicXMLErrorsPresenter>() {
        public TopicXMLErrorsPresenter getInstance(final CreationalContext context) {
            final TopicXMLErrorsPresenter inj4279_TopicXMLErrorsPresenter = new TopicXMLErrorsPresenter();
            context.addBean(context.getBeanReference(TopicXMLErrorsPresenter.class, arrayOf_19635043Annotation_27515521), inj4279_TopicXMLErrorsPresenter);
            _$1893219218_display(inj4279_TopicXMLErrorsPresenter, inj4256_TopicXMLErrorsView_creational.getInstance(context));
            _1500219897_eventBus(inj4279_TopicXMLErrorsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4279_TopicXMLErrorsPresenter;
        }
    };
    private final CreationalCallback<TopicRevisionsPresenter> inj4282_TopicRevisionsPresenter_creational = new CreationalCallback<TopicRevisionsPresenter>() {
        public TopicRevisionsPresenter getInstance(final CreationalContext context) {
            final TopicRevisionsPresenter inj4281_TopicRevisionsPresenter = new TopicRevisionsPresenter();
            context.addBean(context.getBeanReference(TopicRevisionsPresenter.class, arrayOf_19635043Annotation_27515521), inj4281_TopicRevisionsPresenter);
            _1500219897_eventBus(inj4281_TopicRevisionsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4281_TopicRevisionsPresenter;
        }
    };
    private final CreationalCallback<ImageFilteredResultsPresenter> inj4284_ImageFilteredResultsPresenter_creational = new CreationalCallback<ImageFilteredResultsPresenter>() {
        public ImageFilteredResultsPresenter getInstance(final CreationalContext context) {
            final ImageFilteredResultsPresenter inj4283_ImageFilteredResultsPresenter = new ImageFilteredResultsPresenter();
            context.addBean(context.getBeanReference(ImageFilteredResultsPresenter.class, arrayOf_19635043Annotation_27515521), inj4283_ImageFilteredResultsPresenter);
            _$1131523351_display(inj4283_ImageFilteredResultsPresenter, inj4271_ImageFilteredResultsView_creational.getInstance(context));
            _1500219897_eventBus(inj4283_ImageFilteredResultsPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4283_ImageFilteredResultsPresenter;
        }
    };
    private final CreationalCallback<ImagePresenter> inj4286_ImagePresenter_creational = new CreationalCallback<ImagePresenter>() {
        public ImagePresenter getInstance(final CreationalContext context) {
            final ImagePresenter inj4285_ImagePresenter = new ImagePresenter();
            context.addBean(context.getBeanReference(ImagePresenter.class, arrayOf_19635043Annotation_27515521), inj4285_ImagePresenter);
            _$550803640_display(inj4285_ImagePresenter, inj4272_ImageView_creational.getInstance(context));
            _1500219897_eventBus(inj4285_ImagePresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4285_ImagePresenter;
        }
    };
    private final CreationalCallback<TopicRenderedPresenter> inj4288_TopicRenderedPresenter_creational = new CreationalCallback<TopicRenderedPresenter>() {
        public TopicRenderedPresenter getInstance(final CreationalContext context) {
            final TopicRenderedPresenter inj4287_TopicRenderedPresenter = new TopicRenderedPresenter();
            context.addBean(context.getBeanReference(TopicRenderedPresenter.class, arrayOf_19635043Annotation_27515521), inj4287_TopicRenderedPresenter);
            _1500219897_eventBus(inj4287_TopicRenderedPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4287_TopicRenderedPresenter;
        }
    };
    private final CreationalCallback<TopicXMLPresenter> inj4290_TopicXMLPresenter_creational = new CreationalCallback<TopicXMLPresenter>() {
        public TopicXMLPresenter getInstance(final CreationalContext context) {
            final TopicXMLPresenter inj4289_TopicXMLPresenter = new TopicXMLPresenter();
            context.addBean(context.getBeanReference(TopicXMLPresenter.class, arrayOf_19635043Annotation_27515521), inj4289_TopicXMLPresenter);
            _1500219897_eventBus(inj4289_TopicXMLPresenter, _$477517530_produceEventBus(inj4220_App));
            return inj4289_TopicXMLPresenter;
        }
    };

    static class AppController_inj4222_proxy extends AppController {
        private AppController $$_proxy_$$;

        public void bind() {
            $$_proxy_$$.bind();
        }

        public void go(HasWidgets a0) {
            $$_proxy_$$.go(a0);
        }

        public void onValueChange(ValueChangeEvent a0) {
            $$_proxy_$$.onValueChange(a0);
        }

        public int hashCode() {
            if ($$_proxy_$$ == null) {
                throw new IllegalStateException("call to hashCode() on an unclosed proxy.");
            } else {
                return $$_proxy_$$.hashCode();
            }
        }

        public boolean equals(Object o) {
            if ($$_proxy_$$ == null) {
                throw new IllegalStateException("call to equal() on an unclosed proxy.");
            } else {
                return $$_proxy_$$.equals(o);
            }
        }

        public void __$setProxiedInstance$(AppController proxy) {
            $$_proxy_$$ = proxy;
        }
    }

    private void declareBeans_0() {
        injContext.addBean(IOCBeanManagerProvider.class, IOCBeanManagerProvider.class, inj4219_IOCBeanManagerProvider_creational, inj4206_IOCBeanManagerProvider, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(Provider.class, IOCBeanManagerProvider.class, inj4219_IOCBeanManagerProvider_creational, inj4206_IOCBeanManagerProvider, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(App.class, App.class, inj4221_App_creational, inj4220_App, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(HandlerManager.class, HandlerManager.class, inj4198_HandlerManager_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(HasHandlers.class, HandlerManager.class, inj4198_HandlerManager_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(AppController.class, AppController.class, inj4224_AppController_creational, inj4223_AppController, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(PresenterInterface.class, AppController.class, inj4224_AppController_creational, inj4223_AppController, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ValueChangeHandler.class, AppController.class, inj4224_AppController_creational, inj4223_AppController, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(EventHandler.class, AppController.class, inj4224_AppController_creational, inj4223_AppController, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicView.class, TopicView.class, inj4227_TopicView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(Display.class, TopicView.class, inj4227_TopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicView.class, inj4227_TopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicView.class, inj4227_TopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicView.class, inj4227_TopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicView.class, inj4227_TopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicPresenter.class, TopicPresenter.class, inj4226_TopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicPresenter.class, inj4226_TopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicPresenter.class, inj4226_TopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(SearchResultsView.class, SearchResultsView.class, inj4230_SearchResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsPresenter.Display.class, SearchResultsView.class, inj4230_SearchResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, SearchResultsView.class, inj4230_SearchResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, SearchResultsView.class, inj4230_SearchResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(SearchResultsPresenter.class, SearchResultsPresenter.class, inj4229_SearchResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, SearchResultsPresenter.class, inj4229_SearchResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, SearchResultsPresenter.class, inj4229_SearchResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, SearchResultsPresenter.class, inj4229_SearchResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(CategoryView.class, CategoryView.class, inj4233_CategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryPresenter.Display.class, CategoryView.class, inj4233_CategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, CategoryView.class, inj4233_CategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, CategoryView.class, inj4233_CategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(CategoryPresenter.class, CategoryPresenter.class, inj4232_CategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, CategoryPresenter.class, inj4232_CategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, CategoryPresenter.class, inj4232_CategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, CategoryPresenter.class, inj4232_CategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagsFilteredResultsAndTagView.class, TagsFilteredResultsAndTagView.class, inj4236_TagsFilteredResultsAndTagView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(TagsFilteredResultsAndDetailsPresenter.Display.class, TagsFilteredResultsAndTagView.class, inj4236_TagsFilteredResultsAndTagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TagsFilteredResultsAndTagView.class, inj4236_TagsFilteredResultsAndTagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TagsFilteredResultsAndTagView.class, inj4236_TagsFilteredResultsAndTagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagFilteredResultsView.class, TagFilteredResultsView.class, inj4237_TagFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagFilteredResultsPresenter.Display.class, TagFilteredResultsView.class, inj4237_TagFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TagFilteredResultsView.class, inj4237_TagFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TagFilteredResultsView.class, inj4237_TagFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagView.class, TagView.class, inj4238_TagView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagPresenter.Display.class, TagView.class, inj4238_TagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagViewInterface.class, TagView.class, inj4238_TagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TagView.class, inj4238_TagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagViewBase.class, TagView.class, inj4238_TagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TagView.class, inj4238_TagView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagProjectsView.class, TagProjectsView.class, inj4239_TagProjectsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagProjectsPresenter.Display.class, TagProjectsView.class, inj4239_TagProjectsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagViewInterface.class, TagProjectsView.class, inj4239_TagProjectsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TagProjectsView.class, inj4239_TagProjectsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagViewBase.class, TagProjectsView.class, inj4239_TagProjectsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TagProjectsView.class, inj4239_TagProjectsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagCategoriesView.class, TagCategoriesView.class, inj4240_TagCategoriesView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagCategoriesPresenter.Display.class, TagCategoriesView.class, inj4240_TagCategoriesView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagViewInterface.class, TagCategoriesView.class, inj4240_TagCategoriesView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TagCategoriesView.class, inj4240_TagCategoriesView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagViewBase.class, TagCategoriesView.class, inj4240_TagCategoriesView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TagCategoriesView.class, inj4240_TagCategoriesView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagsFilteredResultsAndDetailsPresenter.class, TagsFilteredResultsAndDetailsPresenter.class, inj4235_TagsFilteredResultsAndTagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(CategoryPresenterBase.class, TagsFilteredResultsAndDetailsPresenter.class, inj4235_TagsFilteredResultsAndTagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(EditableView.class, TagsFilteredResultsAndDetailsPresenter.class, inj4235_TagsFilteredResultsAndTagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, TagsFilteredResultsAndDetailsPresenter.class, inj4235_TagsFilteredResultsAndTagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TagsFilteredResultsAndDetailsPresenter.class, inj4235_TagsFilteredResultsAndTagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagFilteredResultsPresenter.class, TagFilteredResultsPresenter.class, inj4242_TagFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, TagFilteredResultsPresenter.class, inj4242_TagFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, TagFilteredResultsPresenter.class, inj4242_TagFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TagFilteredResultsPresenter.class, inj4242_TagFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicTagsView.class, TopicTagsView.class, inj4245_TopicTagsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicTagsPresenter.Display.class, TopicTagsView.class, inj4245_TopicTagsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicTagsView.class, inj4245_TopicTagsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicTagsView.class, inj4245_TopicTagsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicTagsView.class, inj4245_TopicTagsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicTagsView.class, inj4245_TopicTagsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicTagsPresenter.class, TopicTagsPresenter.class, inj4244_TopicTagsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicTagsPresenter.class, inj4244_TopicTagsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicTagsPresenter.class, inj4244_TopicTagsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(WelcomeView.class, WelcomeView.class, inj4248_WelcomeView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.WelcomePresenter.Display.class, WelcomeView.class, inj4248_WelcomeView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, WelcomeView.class, inj4248_WelcomeView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, WelcomeView.class, inj4248_WelcomeView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(WelcomePresenter.class, WelcomePresenter.class, inj4247_WelcomePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, WelcomePresenter.class, inj4247_WelcomePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, WelcomePresenter.class, inj4247_WelcomePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, WelcomePresenter.class, inj4247_WelcomePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagPresenter.class, TagPresenter.class, inj4250_TagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, TagPresenter.class, inj4250_TagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, TagPresenter.class, inj4250_TagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TagPresenter.class, inj4250_TagPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(SearchResultsAndTopicView.class, SearchResultsAndTopicView.class, inj4253_SearchResultsAndTopicView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter.Display.class, SearchResultsAndTopicView.class, inj4253_SearchResultsAndTopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, SearchResultsAndTopicView.class, inj4253_SearchResultsAndTopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, SearchResultsAndTopicView.class, inj4253_SearchResultsAndTopicView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicXMLView.class, TopicXMLView.class, inj4254_TopicXMLView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLPresenter.Display.class, TopicXMLView.class, inj4254_TopicXMLView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicXMLView.class, inj4254_TopicXMLView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicXMLView.class, inj4254_TopicXMLView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicXMLView.class, inj4254_TopicXMLView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicXMLView.class, inj4254_TopicXMLView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicRenderedView.class, TopicRenderedView.class, inj4255_TopicRenderedView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRenderedPresenter.Display.class, TopicRenderedView.class, inj4255_TopicRenderedView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicRenderedView.class, inj4255_TopicRenderedView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicRenderedView.class, inj4255_TopicRenderedView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicRenderedView.class, inj4255_TopicRenderedView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicRenderedView.class, inj4255_TopicRenderedView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicXMLErrorsView.class, TopicXMLErrorsView.class, inj4256_TopicXMLErrorsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLErrorsPresenter.Display.class, TopicXMLErrorsView.class, inj4256_TopicXMLErrorsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicXMLErrorsView.class, inj4256_TopicXMLErrorsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicXMLErrorsView.class, inj4256_TopicXMLErrorsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicXMLErrorsView.class, inj4256_TopicXMLErrorsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicXMLErrorsView.class, inj4256_TopicXMLErrorsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicBugsView.class, TopicBugsView.class, inj4257_TopicBugsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicBugsPresenter.Display.class, TopicBugsView.class, inj4257_TopicBugsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicBugsView.class, inj4257_TopicBugsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicBugsView.class, inj4257_TopicBugsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicBugsView.class, inj4257_TopicBugsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicBugsView.class, inj4257_TopicBugsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicRevisionsView.class, TopicRevisionsView.class, inj4258_TopicRevisionsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRevisionsPresenter.Display.class, TopicRevisionsView.class, inj4258_TopicRevisionsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewInterface.class, TopicRevisionsView.class, inj4258_TopicRevisionsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, TopicRevisionsView.class, inj4258_TopicRevisionsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicViewBase.class, TopicRevisionsView.class, inj4258_TopicRevisionsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, TopicRevisionsView.class, inj4258_TopicRevisionsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(SearchResultsAndTopicPresenter.class, SearchResultsAndTopicPresenter.class, inj4252_SearchResultsAndTopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, SearchResultsAndTopicPresenter.class, inj4252_SearchResultsAndTopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, SearchResultsAndTopicPresenter.class, inj4252_SearchResultsAndTopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, SearchResultsAndTopicPresenter.class, inj4252_SearchResultsAndTopicPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(SearchView.class, SearchView.class, inj4261_SearchView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchPresenter.Display.class, SearchView.class, inj4261_SearchView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, SearchView.class, inj4261_SearchView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, SearchView.class, inj4261_SearchView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(SearchPresenter.class, SearchPresenter.class, inj4260_SearchPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, SearchPresenter.class, inj4260_SearchPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, SearchPresenter.class, inj4260_SearchPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, SearchPresenter.class, inj4260_SearchPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(CategoryFilteredResultsView.class, CategoryFilteredResultsView.class, inj4264_CategoryFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryFilteredResultsPresenter.Display.class, CategoryFilteredResultsView.class, inj4264_CategoryFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, CategoryFilteredResultsView.class, inj4264_CategoryFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, CategoryFilteredResultsView.class, inj4264_CategoryFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(CategoryFilteredResultsPresenter.class, CategoryFilteredResultsPresenter.class, inj4263_CategoryFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, CategoryFilteredResultsPresenter.class, inj4263_CategoryFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, CategoryFilteredResultsPresenter.class, inj4263_CategoryFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, CategoryFilteredResultsPresenter.class, inj4263_CategoryFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(CategoriesFilteredResultsAndCategoryView.class, CategoriesFilteredResultsAndCategoryView.class, inj4267_CategoriesFilteredResultsAndCategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(CategoriesFilteredResultsAndDetailsPresenter.Display.class, CategoriesFilteredResultsAndCategoryView.class, inj4267_CategoriesFilteredResultsAndCategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, CategoriesFilteredResultsAndCategoryView.class, inj4267_CategoriesFilteredResultsAndCategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, CategoriesFilteredResultsAndCategoryView.class, inj4267_CategoriesFilteredResultsAndCategoryView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(CategoriesFilteredResultsAndDetailsPresenter.class, CategoriesFilteredResultsAndDetailsPresenter.class, inj4266_CategoriesFilteredResultsAndCategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, CategoriesFilteredResultsAndDetailsPresenter.class, inj4266_CategoriesFilteredResultsAndCategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, CategoriesFilteredResultsAndDetailsPresenter.class, inj4266_CategoriesFilteredResultsAndCategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, CategoriesFilteredResultsAndDetailsPresenter.class, inj4266_CategoriesFilteredResultsAndCategoryPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImagesFilteredResultsAndImageView.class, ImagesFilteredResultsAndImageView.class, inj4270_ImagesFilteredResultsAndImageView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(ImagesFilteredResultsAndDetailsPresenter.Display.class, ImagesFilteredResultsAndImageView.class, inj4270_ImagesFilteredResultsAndImageView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, ImagesFilteredResultsAndImageView.class, inj4270_ImagesFilteredResultsAndImageView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, ImagesFilteredResultsAndImageView.class, inj4270_ImagesFilteredResultsAndImageView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImageFilteredResultsView.class, ImageFilteredResultsView.class, inj4271_ImageFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImageFilteredResultsPresenter.Display.class, ImageFilteredResultsView.class, inj4271_ImageFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, ImageFilteredResultsView.class, inj4271_ImageFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, ImageFilteredResultsView.class, inj4271_ImageFilteredResultsView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImageView.class, ImageView.class, inj4272_ImageView_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagePresenter.Display.class, ImageView.class, inj4272_ImageView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateViewInterface.class, ImageView.class, inj4272_ImageView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplateView.class, ImageView.class, inj4272_ImageView_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImagesFilteredResultsAndDetailsPresenter.class, ImagesFilteredResultsAndDetailsPresenter.class, inj4269_ImagesFilteredResultsAndImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, ImagesFilteredResultsAndDetailsPresenter.class, inj4269_ImagesFilteredResultsAndImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImagePresenterBase.class, ImagesFilteredResultsAndDetailsPresenter.class, inj4269_ImagesFilteredResultsAndImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, ImagesFilteredResultsAndDetailsPresenter.class, inj4269_ImagesFilteredResultsAndImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, ImagesFilteredResultsAndDetailsPresenter.class, inj4269_ImagesFilteredResultsAndImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagCategoriesPresenter.class, TagCategoriesPresenter.class, inj4274_TagCategoriesPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, TagCategoriesPresenter.class, inj4274_TagCategoriesPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, TagCategoriesPresenter.class, inj4274_TagCategoriesPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TagCategoriesPresenter.class, inj4274_TagCategoriesPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicBugsPresenter.class, TopicBugsPresenter.class, inj4276_TopicBugsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicBugsPresenter.class, inj4276_TopicBugsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicBugsPresenter.class, inj4276_TopicBugsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TagProjectsPresenter.class, TagProjectsPresenter.class, inj4278_TagProjectsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, TagProjectsPresenter.class, inj4278_TagProjectsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, TagProjectsPresenter.class, inj4278_TagProjectsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TagProjectsPresenter.class, inj4278_TagProjectsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicXMLErrorsPresenter.class, TopicXMLErrorsPresenter.class, inj4280_TopicXMLErrorsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicXMLErrorsPresenter.class, inj4280_TopicXMLErrorsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicXMLErrorsPresenter.class, inj4280_TopicXMLErrorsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicRevisionsPresenter.class, TopicRevisionsPresenter.class, inj4282_TopicRevisionsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicRevisionsPresenter.class, inj4282_TopicRevisionsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicRevisionsPresenter.class, inj4282_TopicRevisionsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImageFilteredResultsPresenter.class, ImageFilteredResultsPresenter.class, inj4284_ImageFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(EditableView.class, ImageFilteredResultsPresenter.class, inj4284_ImageFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, ImageFilteredResultsPresenter.class, inj4284_ImageFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, ImageFilteredResultsPresenter.class, inj4284_ImageFilteredResultsPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(ImagePresenter.class, ImagePresenter.class, inj4286_ImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(ImagePresenterBase.class, ImagePresenter.class, inj4286_ImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(BaseTemplatePresenterInterface.class, ImagePresenter.class, inj4286_ImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, ImagePresenter.class, inj4286_ImagePresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicRenderedPresenter.class, TopicRenderedPresenter.class, inj4288_TopicRenderedPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicRenderedPresenter.class, inj4288_TopicRenderedPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicRenderedPresenter.class, inj4288_TopicRenderedPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(TopicXMLPresenter.class, TopicXMLPresenter.class, inj4290_TopicXMLPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, true);
        injContext.addBean(BaseTemplatePresenterInterface.class, TopicXMLPresenter.class, inj4290_TopicXMLPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
        injContext.addBean(PresenterInterface.class, TopicXMLPresenter.class, inj4290_TopicXMLPresenter_creational, null, arrayOf_19635043Annotation_27515521, null, false);
    }

    private native static void _$550803640_display(ImagePresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagePresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagePresenter::display = value;
	}-*/;

    private native static void _1567125089_searchResultsDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::searchResultsDisplay = value;
	}-*/;

    private native static void _1478143022_display(WelcomePresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.WelcomePresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.WelcomePresenter::display = value;
	}-*/;

    private native static void _$147774578_display(TagCategoriesPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagCategoriesPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagCategoriesPresenter::display = value;
	}-*/;

    private native static void _1567125089_topicXMLDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicXMLDisplay = value;
	}-*/;

    private native static void _$525173285_filteredResultsDisplay(TagsFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagFilteredResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagsFilteredResultsAndDetailsPresenter::filteredResultsDisplay = value;
	}-*/;

    private native static void _392006560_display(TopicPresenter instance, Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicPresenter::display = value;
	}-*/;

    private native static void _$1131523351_display(ImageFilteredResultsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImageFilteredResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImageFilteredResultsPresenter::display = value;
	}-*/;

    private native static void _$525173285_projectsDisplay(TagsFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagProjectsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagsFilteredResultsAndDetailsPresenter::projectsDisplay = value;
	}-*/;

    private native static void _1567125089_display(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::display = value;
	}-*/;

    private native static void _$192804415_display(SearchPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchPresenter::display = value;
	}-*/;

    private native static void _954332697_display(SearchResultsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsPresenter::display = value;
	}-*/;

    private native static void _491824760_imageDisplay(ImagesFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagePresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagesFilteredResultsAndDetailsPresenter::imageDisplay = value;
	}-*/;

    private native static void _1500219897_eventBus(BaseTemplatePresenterInterface instance, HandlerManager value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface::eventBus = value;
	}-*/;

    private native static void _1643984526_display(CategoriesFilteredResultsAndDetailsPresenter instance, CategoriesFilteredResultsAndDetailsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoriesFilteredResultsAndDetailsPresenter::display = value;
	}-*/;

    private native static void _$525173285_resultDisplay(TagsFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagsFilteredResultsAndDetailsPresenter::resultDisplay = value;
	}-*/;

    private native static void _$192804415_eventBus(SearchPresenter instance, HandlerManager value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchPresenter::eventBus = value;
	}-*/;

    private native static void _1567125089_topicRevisionsDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRevisionsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicRevisionsDisplay = value;
	}-*/;

    private native static void _$774116150_display(TagPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagPresenter::display = value;
	}-*/;

    private native static void _2098016610_manager(AppController instance, IOCBeanManager value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.AppController::manager = value;
	}-*/;

    private native static void _$1369662608_display(TagProjectsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagProjectsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagProjectsPresenter::display = value;
	}-*/;

    private native static void _$525173285_display(TagsFilteredResultsAndDetailsPresenter instance, TagsFilteredResultsAndDetailsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagsFilteredResultsAndDetailsPresenter::display = value;
	}-*/;

    private native static void _1748359463_display(TagFilteredResultsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagFilteredResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagFilteredResultsPresenter::display = value;
	}-*/;

    private native static void _491824760_imageFilteredResultsDisplay(ImagesFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImageFilteredResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagesFilteredResultsAndDetailsPresenter::imageFilteredResultsDisplay = value;
	}-*/;

    private native static void _1643984526_resultDisplay(CategoriesFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoriesFilteredResultsAndDetailsPresenter::resultDisplay = value;
	}-*/;

    private native static void _1814806242_display(CategoryPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryPresenter::display = value;
	}-*/;

    private native static void _$477517530_appController(App instance, AppController value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.App::appController = value;
	}-*/;

    private native static void _1567125089_topicSplitPanelRenderedDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRenderedPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicSplitPanelRenderedDisplay = value;
	}-*/;

    private native static void _954332697_topicViewDisplay(SearchResultsPresenter instance, Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsPresenter::topicViewDisplay = value;
	}-*/;

    private native static void _1567125089_topicViewDisplay(SearchResultsAndTopicPresenter instance, Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicViewDisplay = value;
	}-*/;

    private native static void _1567125089_topicTagsDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicTagsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicTagsDisplay = value;
	}-*/;

    private native static void _1643984526_filteredResultsDisplay(CategoriesFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryFilteredResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoriesFilteredResultsAndDetailsPresenter::filteredResultsDisplay = value;
	}-*/;

    private native static void _$743140337_display(CategoryFilteredResultsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryFilteredResultsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.category.CategoryFilteredResultsPresenter::display = value;
	}-*/;

    private native static void _2098016610_eventBus(AppController instance, HandlerManager value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.AppController::eventBus = value;
	}-*/;

    private native static void _1567125089_topicBugsDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicBugsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicBugsDisplay = value;
	}-*/;

    private native static void _$1893219218_display(TopicXMLErrorsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLErrorsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLErrorsPresenter::display = value;
	}-*/;

    private native static void _$525173285_categoriesDisplay(TagsFilteredResultsAndDetailsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagCategoriesPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag.TagsFilteredResultsAndDetailsPresenter::categoriesDisplay = value;
	}-*/;

    private native static void _$1522350201_display(TopicTagsPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicTagsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicTagsPresenter::display = value;
	}-*/;

    private native static void _491824760_display(ImagesFilteredResultsAndDetailsPresenter instance, ImagesFilteredResultsAndDetailsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImagesFilteredResultsAndDetailsPresenter::display = value;
	}-*/;

    private native static void _1567125089_topicRenderedDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRenderedPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicRenderedDisplay = value;
	}-*/;

    private native static void _1567125089_topicXMLErrorsDisplay(SearchResultsAndTopicPresenter instance, org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLErrorsPresenter.Display value) /*-{
		instance.@org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter::topicXMLErrorsDisplay = value;
	}-*/;

    public native static HandlerManager _$477517530_produceEventBus(App instance) /*-{
		return instance.@org.jboss.pressgang.ccms.ui.client.local.App::produceEventBus()();
	}-*/;

    // The main IOC bootstrap method.
    public BootstrapperInjectionContext bootstrapContainer() {
        declareBeans_0();
        return injContext;
    }
}