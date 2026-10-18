function Pagination({page, totalPages, onChange})  {

    if (totalPages <= 1)
        return null


    const pageNumbers = Array.from({length: totalPages}, (_, i) => i)
    return (
        <nav className="pagination">
            <button disabled={page === 0} onClick={() => onChange(page -1)}>
                Prev
            </button>

            {pageNumbers.map((number) => (
                <button
                    key={number}
                    className={number === page? 'current' : undefined}
                    onClick={()=>onChange(number)}
                    >
                    {number+1}
                </button>
            ))}


            <button disabled={page +1 >= totalPages} onClick={() => onChange(page +1)}>
                Next
            </button>

        </nav>


    )
}
export default Pagination