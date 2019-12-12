package pl.edu.wat.wcy.blackduck.di

import dagger.Module
import dagger.Provides
import pl.edu.wat.wcy.blackduck.ui.add.AddContract
import pl.edu.wat.wcy.blackduck.ui.add.AddPresenter
import pl.edu.wat.wcy.blackduck.ui.comment.CommentContract
import pl.edu.wat.wcy.blackduck.ui.comment.CommentPresenter
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationContract
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationPresenter
import pl.edu.wat.wcy.blackduck.ui.editprofile.EditProfileContract
import pl.edu.wat.wcy.blackduck.ui.editprofile.EditProfilePresenter
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfileContract
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfilePresenter
import pl.edu.wat.wcy.blackduck.ui.home.HomeContract
import pl.edu.wat.wcy.blackduck.ui.home.HomePresenter
import pl.edu.wat.wcy.blackduck.ui.login.LoginContract
import pl.edu.wat.wcy.blackduck.ui.login.LoginPresenter
import pl.edu.wat.wcy.blackduck.ui.main.MainContract
import pl.edu.wat.wcy.blackduck.ui.main.MainPresenter
import pl.edu.wat.wcy.blackduck.ui.message.MessageContract
import pl.edu.wat.wcy.blackduck.ui.message.MessagePresenter
import pl.edu.wat.wcy.blackduck.ui.post.PostContract
import pl.edu.wat.wcy.blackduck.ui.post.PostPresenter
import pl.edu.wat.wcy.blackduck.ui.profile.ProfileContract
import pl.edu.wat.wcy.blackduck.ui.profile.ProfilePresenter
import pl.edu.wat.wcy.blackduck.ui.register.RegisterContract
import pl.edu.wat.wcy.blackduck.ui.register.RegisterPresenter
import pl.edu.wat.wcy.blackduck.ui.search.SearchContract
import pl.edu.wat.wcy.blackduck.ui.search.SearchPresenter
import pl.edu.wat.wcy.blackduck.ui.settings.SettingsContract
import pl.edu.wat.wcy.blackduck.ui.settings.SettingsPresenter
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun provideMainPresenter() : MainContract.Presenter = MainPresenter()

    @Provides
    @Singleton
    fun provideEditProfilePresenter() : EditProfileContract.Presenter = EditProfilePresenter()

    @Provides
    @Singleton
    fun provideLoginPresenter() : LoginContract.Presenter = LoginPresenter()

    @Provides
    @Singleton
    fun provideMessagePresenter() : MessageContract.Presenter = MessagePresenter()

    @Provides
    @Singleton
    fun provideConversationPresenter() : ConversationContract.Presenter = ConversationPresenter()

    @Provides
    @Singleton
    fun provideHomePresenter() : HomeContract.Presenter = HomePresenter()

    @Provides
    @Singleton
    fun provideProfilePresenter() : ProfileContract.Presenter = ProfilePresenter()

    @Provides
    @Singleton
    fun provideSearchPresenter() : SearchContract.Presenter = SearchPresenter()

    @Provides
    @Singleton
    fun provideSettingsPresenter() : SettingsContract.Presenter = SettingsPresenter()

    @Provides
    @Singleton
    fun provideRegisterPresenter() : RegisterContract.Presenter = RegisterPresenter()

    @Provides
    @Singleton
    fun provideCommentPresenter() : CommentContract.Presenter = CommentPresenter()

    @Provides
    @Singleton
    fun provideAddPresenter() : AddContract.Presenter = AddPresenter()

    @Provides
    @Singleton
    fun providePostPresenter() : PostContract.Presenter = PostPresenter()

    @Provides
    @Singleton
    fun provideForeignProfilePresenter() : ForeignProfileContract.Presenter = ForeignProfilePresenter()

}