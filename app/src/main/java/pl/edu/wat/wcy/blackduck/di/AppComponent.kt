package pl.edu.wat.wcy.blackduck.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pl.edu.wat.wcy.blackduck.ui.add.AddFragment
import pl.edu.wat.wcy.blackduck.ui.add.AddPresenter
import pl.edu.wat.wcy.blackduck.ui.comment.CommentActivity
import pl.edu.wat.wcy.blackduck.ui.comment.CommentPresenter
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationActivity
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationAdapter
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationPresenter
import pl.edu.wat.wcy.blackduck.ui.editprofile.EditProfileActivity
import pl.edu.wat.wcy.blackduck.ui.editprofile.EditProfilePresenter
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfilePresenter
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfileActivity
import pl.edu.wat.wcy.blackduck.ui.home.HomeAdapter
import pl.edu.wat.wcy.blackduck.ui.home.HomeFragment
import pl.edu.wat.wcy.blackduck.ui.home.HomePresenter
import pl.edu.wat.wcy.blackduck.ui.login.LoginFragment
import pl.edu.wat.wcy.blackduck.ui.login.LoginPresenter
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.MainPresenter
import pl.edu.wat.wcy.blackduck.ui.message.MessageActivity
import pl.edu.wat.wcy.blackduck.ui.message.MessagePresenter
import pl.edu.wat.wcy.blackduck.ui.message.MessagesAdapter
import pl.edu.wat.wcy.blackduck.ui.post.PostActivity
import pl.edu.wat.wcy.blackduck.ui.post.PostPresenter
import pl.edu.wat.wcy.blackduck.ui.profile.ProfileFragment
import pl.edu.wat.wcy.blackduck.ui.profile.ProfilePresenter
import pl.edu.wat.wcy.blackduck.ui.register.RegisterFragment
import pl.edu.wat.wcy.blackduck.ui.register.RegisterPresenter
import pl.edu.wat.wcy.blackduck.ui.search.SearchFragment
import pl.edu.wat.wcy.blackduck.ui.search.SearchPostAdapter
import pl.edu.wat.wcy.blackduck.ui.search.SearchPresenter
import pl.edu.wat.wcy.blackduck.ui.search.SearchUserAdapter
import pl.edu.wat.wcy.blackduck.ui.settings.SettingsFragment
import pl.edu.wat.wcy.blackduck.ui.settings.SettingsPresenter
import pl.edu.wat.wcy.blackduck.util.MyFirebaseMessagingService
import javax.inject.Singleton

@Singleton
@Component(modules = [ PresenterModule::class, PreferencesModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(myFirebaseMessagingService: MyFirebaseMessagingService)

    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)

    fun inject(editProfileActivity: EditProfileActivity)
    fun inject(editProfilePresenter: EditProfilePresenter)

    fun inject(loginFragment: LoginFragment)
    fun inject(loginPresenter: LoginPresenter)

    fun inject(messageActivity: MessageActivity)
    fun inject(messagePresenter: MessagePresenter)

    fun inject(conversationActivity: ConversationActivity)
    fun inject(conversationPresenter: ConversationPresenter)

    fun inject(commentActivity: CommentActivity)
    fun inject(commentPresenter: CommentPresenter)

    fun inject(homeFragment: HomeFragment)
    fun inject(homePresenter: HomePresenter)

    fun inject(profileFragment: ProfileFragment)
    fun inject(profilePresenter: ProfilePresenter)

    fun inject(searchFragment: SearchFragment)
    fun inject(searchPresenter: SearchPresenter)

    fun inject(settingsFragment: SettingsFragment)
    fun inject(settingsPresenter: SettingsPresenter)

    fun inject(registerFragment: RegisterFragment)
    fun inject(registerPresenter: RegisterPresenter)
    
    fun inject(addFragment: AddFragment)
    fun inject(addPresenter: AddPresenter)

    fun inject(postActivity: PostActivity)
    fun inject(postPresenter: PostPresenter)

    fun inject(foreignProfileActivity: ForeignProfileActivity)
    fun inject(foreignProfilePresenter: ForeignProfilePresenter)

    fun inject(homeAdapter: HomeAdapter)
    fun inject(messagesAdapter: MessagesAdapter)
    fun inject(conversationAdapter: ConversationAdapter)
    fun inject(searchUserAdapter: SearchUserAdapter)
    fun inject(searchPostAdapter: SearchPostAdapter)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun provideContext(context: Context): Builder

        fun build(): AppComponent
    }

}