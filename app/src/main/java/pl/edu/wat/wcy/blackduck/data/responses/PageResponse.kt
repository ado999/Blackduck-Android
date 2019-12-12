package pl.edu.wat.wcy.blackduck.data.responses

data class PageResponse<T>(
    val content: ArrayList<T>?,
    val pageable: Pageable,
    val last: Boolean,
    val totalElements: Int,
    val totalPages: Int,
    val sort: Sort,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
){
    data class Pageable(
        val sort: Sort,
        val offset: Int,
        val pageNumber: Int,
        val pageSize: Int,
        val paged: Boolean,
        val unpaged: Boolean
    )

    data class Sort(
        val sorted: Boolean,
        val unsorted: Boolean,
        val empty: Boolean
    )
}