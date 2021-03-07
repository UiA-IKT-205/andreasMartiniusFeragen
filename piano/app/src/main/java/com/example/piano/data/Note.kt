
data class  Note(val note: String, val delta:Long) {
    override fun toString(): String {
        return "$note, $delta"
    }
}