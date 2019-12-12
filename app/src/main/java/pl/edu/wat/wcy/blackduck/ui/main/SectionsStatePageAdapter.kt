package pl.edu.wat.wcy.blackduck.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SectionsStatePageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    enum class FragmentName{
        LOGIN,
        REGISTER,
        HOME,
        SEARCH,
        ADD,
        HEART,
        PROFILE,
        SETTINGS
    }

    private val fragments = ArrayList<Fragment>()
    private val fragNames = ArrayList<FragmentName>()

    override fun getItem(i: Int): Fragment? {
        return fragments[i]
    }

    fun getItemIndex(str: FragmentName) : Int {
        return fragNames.indexOf(str)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addItem(name : FragmentName, fragment: Fragment){
        fragments.add(fragment)
        fragNames.add(name)
    }
}